package com.arranbailey.dextracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.model.toEntity
import com.arranbailey.dextracker.model.toSetEntity
import com.arranbailey.dextracker.network.RetrofitInstance
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            checkMissingSets()
        }
    }

    fun cacheSet(setId: String) {
        viewModelScope.launch {
            Log.d("LoadingViewModel", "Here")
            try {
                val response = RetrofitInstance.api.searchCards("set.id:$setId", pageSize = 500)
                val entities = response.data.map { it.toEntity() }
                dao.insertAll(entities)
                Log.d("CardViewModel", "Cached ${entities.size} cards for $setId")
            } catch (e: Exception) {
                Log.e("CardViewModel", "Error caching set: ${e.message}")
            }
        }
    }

    suspend fun checkMissingSets() {
        isCaching.value = true

        val cachedSetIds = setDao.getAll().map { it.id }.toSet()
        val fetchedSets = RetrofitInstance.api.searchSets("")
        val entities = fetchedSets.data.map { it.toSetEntity() }
        val missingSets = entities.filter { it.id !in cachedSetIds }

        val total = missingSets.size.coerceAtLeast(1)

        for ((index, entity) in missingSets.withIndex()) {
            progressText.value = "Caching ${entity.name} (${index + 1}/$total)"
            progress.floatValue = (index + 1f) / total

            // Insert the set
//            setDao.insertSet(entity)
//
//            // Fetch and insert the cards for this set
//            val cardsResponse = RetrofitInstance.api.searchCards("set.name:${entity.name}", pageSize = 250)
//            val cardEntities = cardsResponse.data.map { it.toEntity() }
//            dao.insertAll(cardEntities)
            cacheMissingSet(entity)
        }

        progressText.value = "Caching complete!"
        progress.floatValue = 1f
        isCaching.value = false
    }

    suspend fun cacheMissingSet(entity: SetEntity) {
        setDao.insertSet(entity)
        cacheSet(entity.id)
    }

}