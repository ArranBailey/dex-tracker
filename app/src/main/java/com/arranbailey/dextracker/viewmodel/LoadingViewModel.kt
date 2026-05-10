package com.arranbailey.dextracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.model.toEntity
import com.arranbailey.dextracker.model.toSetEntity
import com.arranbailey.dextracker.network.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.withLock
import kotlin.math.log

class LoadingViewModel(application: Application) : AndroidViewModel(application) {
    var isCaching = mutableStateOf(true)
        private set
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
    private val setDao = db.setDao()
    var isLoading = mutableStateOf(false)
        private set

    var progress = mutableFloatStateOf(0f)
        private set

    var progressText = mutableStateOf("Starting...")
        private set

    private val progressMutex = Mutex()
    private var completedSetsCount = 0

    init {
        viewModelScope.launch {
            try {
                checkMissingSets()
            }catch (e: Exception){
                Log.e("NetworkError", "Error fetching data from API: ${e.message}")
                isCaching.value = false
            }

        }
    }

    suspend fun checkMissingSets() {
        isCaching.value = true
        progressText.value = "Checking for new sets..."
        progress.floatValue = 0f
        completedSetsCount = 0

        val cachedSetIds = setDao.getAll().map { it.id }.toSet()
        val allRemoteSets = RetrofitInstance.api.getSets().map { it.toSetEntity() }
        val missingSets = allRemoteSets.filter { it.id !in cachedSetIds }

        if (missingSets.isEmpty()) {
            progressText.value = "All sets are up to date!"
            progress.floatValue = 1f
            isCaching.value = false
            return
        }

        val totalSetsToCache = missingSets.size
        progressText.value = "Found $totalSetsToCache new sets to cache..."

        val cachingJobs = missingSets.map { setEntity ->
            viewModelScope.async { // Launch each as an independent async job
                try {
                    // 1. Insert the set information itself
                    setDao.insertSet(setEntity)
                    Log.d("LoadingViewModel", "Inserted set: ${setEntity.name}")

                    // 2. Fetch and cache cards for this set
                    val setWithCards = RetrofitInstance.api.getSetWithCards(setEntity.id)
                    val cards = setWithCards.cards

                    if (!cards.isNullOrEmpty()) {
                        val cardEntities = cards.map { card ->
                            CardEntity(
                                id = card.id,
                                name = card.name,
                                imageSmall = card.image + "/low.webp",
                                imageLarge = card.image + "/high.webp",
                                rarity = card.rarity,
                                setName = setEntity.name,
                                setId = setEntity.id
                            )
                        }
                        dao.insertAll(cardEntities)
                        Log.d("LoadingViewModel", "Cached ${cardEntities.size} cards for set ${setEntity.name} (${setEntity.id})")
                    }

                    // 3. Update progress safely
                    progressMutex.withLock {
                        completedSetsCount++
                        progress.floatValue = completedSetsCount.toFloat() / totalSetsToCache
                        progressText.value = "Cached: ${setEntity.name} ($completedSetsCount/$totalSetsToCache)"
                    }
                } catch (e: Exception) {
                    Log.e("LoadingViewModel", "Error caching set ${setEntity.name} (${setEntity.id}): ${e.message}", e)
                    // Optionally, update progress for the attempt, or track failures separately
                    // For simplicity, we'll still count it towards progress of "attempted" sets
                    progressMutex.withLock {
                        completedSetsCount++ // Count as an attempt
                        progress.floatValue = completedSetsCount.toFloat() / totalSetsToCache
                        progressText.value = "Error caching: ${setEntity.name}. Continuing... ($completedSetsCount/$totalSetsToCache)"
                    }
                    // Depending on the error, you might want to re-throw or handle it more specifically
                }
            }
        }

        // Wait for all caching jobs to complete (either successfully or with an exception)
        cachingJobs.awaitAll()

        Log.d("LoadingViewModel", "All caching jobs complete. Total sets processed: $completedSetsCount")

        // Final UI update
        val completedSetsCount = null
        if (completedSetsCount == totalSetsToCache) { // Or check against a list of successful caches if you track that
            progressText.value = "Caching complete! $totalSetsToCache sets processed."
        } else {
            progressText.value = "Caching finished. Some sets may have had issues."
        }
        progress.floatValue = 1f
        isCaching.value = false
    }

    // cacheSet and cacheMissingSet are no longer needed as their logic is integrated above.
}

