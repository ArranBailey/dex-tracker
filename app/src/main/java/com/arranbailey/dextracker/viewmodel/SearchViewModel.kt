package com.arranbailey.dextracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.model.CardSet
import com.arranbailey.dextracker.model.toEntity
import com.arranbailey.dextracker.model.toSetEntity
import com.arranbailey.dextracker.network.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var isCaching = mutableStateOf(false)
        private set
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
    private val setDao = db.setDao()
    var cards = mutableStateOf<List<CardEntity>>(emptyList())
        private set
    var sets = mutableStateOf<List<CardSet>>(emptyList())
    var isLoading = mutableStateOf(false)
        private set


    fun search(query: String) {
        viewModelScope.launch {
            cards.value = dao.search(query)
            //cards.value = results.map { it.toCard() }
        }
    }
    fun cacheSet(setName: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchCards("set.name:$setName", pageSize = 250)
                val entities = response.data.map { it.toEntity() }
                dao.insertAll(entities)
                Log.d("CardViewModel", "Cached ${entities.size} cards for $setName")
            } catch (e: Exception) {
                Log.e("CardViewModel", "Error caching set: ${e.message}")
            }
        }
    }

    fun loadCardsFromCache(setName: String) {
        viewModelScope.launch {
            Log.d("RoomTest", "Loading cards for set: $setName")
            val cachedCards = dao.getCardsBySetName(setName)
            Log.d("RoomTest", "Found ${cachedCards.size} cards in DB")
            //cards.value = cachedCards.map { it.toCard() }
        }
    }

    fun checkMissingSets(){
        viewModelScope.launch {
            isCaching.value = true
            var cachedSets = setDao.getAll()
            val cachedSetIds = cachedSets.map { it.id }.toSet()
            val fetchedSets = RetrofitInstance.api.searchSets("")
            val entities = fetchedSets.data.map { it.toSetEntity()}
            for (entity in entities){
                if (entity.id !in cachedSetIds){
                    cacheMissingSet(entity)
                }
            }
            isCaching.value = false
        }
    }

    suspend fun cacheMissingSet(entity: SetEntity){
        setDao.insertSet(entity)
        cacheSet(entity.name)
    }

    fun cacheAllCards(){
        viewModelScope.launch {
            isCaching.value = true
            getAllSets()
            Log.d("Setup", "Saving cards to database")
            val cachedSets = setDao.getAll()
            for (set in cachedSets){
                delay(100)
                Log.d("Setup", "Saving cards in \"${set.name}\"")
                cacheSet("\"${set.name}\"")
            }
            isCaching.value=false
        }
    }

    suspend fun getAllSets() {
        val newSets = RetrofitInstance.api.searchSets("")
        val entities = newSets.data.map { it.toSetEntity() }
        setDao.insertAllSets(entities)
        Log.d("CardViewModel", "Cached ${entities.size} sets")
    }
}

