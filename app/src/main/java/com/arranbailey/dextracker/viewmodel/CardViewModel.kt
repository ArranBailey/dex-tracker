package com.arranbailey.dextracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.model.Card
import com.arranbailey.dextracker.model.toCard
import com.arranbailey.dextracker.model.toEntity
import com.arranbailey.dextracker.network.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CardViewModel(application: Application) : AndroidViewModel(application) {
    var cards = mutableStateOf<List<Card>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    fun search(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.searchCards("name:$query")
                Log.d("DEBUG", "Raw response: ${response.data.size}")
                cards.value = response.data
                val rawJson = Gson().toJson(response)
                Log.d("RAW_JSON", rawJson)
            } catch (e: Exception) {
                // Handle error
                cards.value = emptyList()
            }
            isLoading.value = false
        }
    }
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
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
            val cachedCards = dao.getCardsBySet(setName)
            Log.d("RoomTest", "Found ${cachedCards.size} cards in DB")
            cards.value = cachedCards.map { it.toCard() }
        }
    }
}

