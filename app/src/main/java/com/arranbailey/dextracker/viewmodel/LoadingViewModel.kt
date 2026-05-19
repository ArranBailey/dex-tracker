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
import com.arranbailey.dextracker.model.SetBrief
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
        val excludedSeries = setOf("tcgp", "misc", "tk", "mc")

        // 1. Fetch all series and filter out non-physical
        val allSeries = RetrofitInstance.api.getSeries()
            .filter { it.id !in excludedSeries }

        val allSets = mutableListOf<Triple<String, Int, SetBrief>>()
        for ((index, serie) in allSeries.withIndex()) {
            val serieWithSets = RetrofitInstance.api.getSerieWithSets(serie.id)
            serieWithSets.sets?.forEach { set ->
                if (set.id !in cachedSetIds) {
                    allSets.add(Triple(serie.name, index, set))
                }
            }
        }

        if (allSets.isEmpty()) {
            progressText.value = "All sets are up to date!"
            progress.floatValue = 1f
            isCaching.value = false
            return
        }

        val totalSetsToCache = allSets.size
        progressText.value = "Found $totalSetsToCache new sets to cache..."

        // 3. For each missing set, fetch cards and cache
        val cachingJobs = allSets.map { (serieName, serieIndex, setBrief) ->
            viewModelScope.async {
                try {
                    val setEntity = SetEntity(
                        id = setBrief.id,
                        name = setBrief.name,
                        series = serieName,
                        serieOrder = serieIndex,
                        printedTotal = setBrief.cardCount?.official ?: 0,
                        total = setBrief.cardCount?.total ?: 0,
                        ptcgoCode = "",
                        standardLegal = false,
                        expandedLegal = false,
                        releaseDate = "",
                        symbolUrl = setBrief.symbol ?: "",
                        logoUrl = setBrief.logo ?: ""
                    )
                    setDao.insertSet(setEntity)
                    Log.d("LoadingViewModel", "Inserted set: ${setBrief.name}")

                    // Fetch and cache cards
                    val setWithCards = RetrofitInstance.api.getSetWithCards(setBrief.id)
                    val cards = setWithCards.cards
                    if (!cards.isNullOrEmpty()) {
                        val cardEntities = cards.map { card ->
                            CardEntity(
                                id = card.id,
                                name = card.name,
                                imageSmall = card.image + "/low.webp",
                                imageLarge = card.image + "/high.webp",
                                rarity = card.rarity,
                                setName = setBrief.name,
                                setId = setBrief.id
                            )
                        }
                        dao.insertAll(cardEntities)
                        Log.d("LoadingViewModel", "Cached ${cardEntities.size} cards for ${setBrief.name}")
                    }

                    progressMutex.withLock {
                        completedSetsCount++
                        progress.floatValue = completedSetsCount.toFloat() / totalSetsToCache
                        progressText.value = "Cached: ${setBrief.name} ($completedSetsCount/$totalSetsToCache)"
                    }
                } catch (e: Exception) {
                    Log.e("LoadingViewModel", "Error caching set ${setBrief.name} (${setBrief.id}): ${e.message}", e)
                    progressMutex.withLock {
                        completedSetsCount++
                        progress.floatValue = completedSetsCount.toFloat() / totalSetsToCache
                        progressText.value = "Error caching: ${setBrief.name}. Continuing... ($completedSetsCount/$totalSetsToCache)"
                    }
                }
            }
        }

        cachingJobs.awaitAll()
        progressText.value = "Caching complete! $totalSetsToCache sets processed."
        progress.floatValue = 1f
        isCaching.value = false
    }

    // cacheSet and cacheMissingSet are no longer needed as their logic is integrated above.
}

