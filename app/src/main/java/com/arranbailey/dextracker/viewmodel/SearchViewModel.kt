package com.arranbailey.dextracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.model.toSetEntity
import com.arranbailey.dextracker.network.RetrofitInstance
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var isCaching = mutableStateOf(false)
        private set
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
    private val setDao = db.setDao()
    var cards = mutableStateOf<List<CardEntity>>(emptyList())
        private set
    var isLoading = mutableStateOf(false)
        private set

    fun search(query: String) {
        viewModelScope.launch {
            cards.value = dao.search(query)
        }
    }

    fun cacheSet(setId: String) {
        viewModelScope.launch {
            try {
                val setWithCards = RetrofitInstance.api.getSetWithCards(setId)
                val cards = setWithCards.cards
                if (!cards.isNullOrEmpty()) {
                    val entities = cards.map { card ->
                        CardEntity(
                            id = card.id,
                            name = card.name,
                            imageSmall = card.image + "/low.jpg",
                            imageLarge = card.image + "/high.jpg",
                            rarity = card.rarity,
                            setName = setWithCards.name,
                            setId = setWithCards.id
                        )
                    }
                    dao.insertAll(entities)
                    Log.d("CardViewModel", "Cached ${entities.size} cards for ${setWithCards.name}")
                }
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
        }
    }

    fun checkMissingSets() {
        viewModelScope.launch {
            isCaching.value = true
            val cachedSets = setDao.getAll()
            val cachedSetIds = cachedSets.map { it.id }.toSet()
            val fetchedSets = RetrofitInstance.api.getSets()
            val entities = fetchedSets.map { it.toSetEntity() }
            for (entity in entities) {
                if (entity.id !in cachedSetIds) {
                    cacheMissingSet(entity)
                }
            }
            isCaching.value = false
        }
    }

    suspend fun cacheMissingSet(entity: SetEntity) {
        setDao.insertSet(entity)
        val setWithCards = RetrofitInstance.api.getSetWithCards(entity.id)
        val cards = setWithCards.cards
        if (!cards.isNullOrEmpty()) {
            val cardEntities = cards.map { card ->
                CardEntity(
                    id = card.id,
                    name = card.name,
                    imageSmall = card.image + "/low.jpg",
                    imageLarge = card.image + "/high.jpg",
                    rarity = card.rarity,
                    setName = setWithCards.name,
                    setId = setWithCards.id
                )
            }
            dao.insertAll(cardEntities)
            Log.d("CardViewModel", "Cached ${cardEntities.size} cards for ${entity.name}")
        }
    }

    fun cacheAllCards() {
        viewModelScope.launch {
            isCaching.value = true
            getAllSets()
            Log.d("Setup", "Saving cards to database")
            val cachedSets = setDao.getAll()
            for (set in cachedSets) {
                Log.d("Setup", "Saving cards in \"${set.name}\"")
                val setWithCards = RetrofitInstance.api.getSetWithCards(set.id)
                val cards = setWithCards.cards
                if (!cards.isNullOrEmpty()) {
                    val cardEntities = cards.map { card ->
                        CardEntity(
                            id = card.id,
                            name = card.name,
                            imageSmall = card.image + "/low.jpg",
                            imageLarge = card.image + "/high.jpg",
                            rarity = card.rarity,
                            setName = setWithCards.name,
                            setId = setWithCards.id
                        )
                    }
                    dao.insertAll(cardEntities)
                }
            }
            isCaching.value = false
        }
    }

    suspend fun getAllSets() {
        val newSets = RetrofitInstance.api.getSets()
        val entities = newSets.map { it.toSetEntity() }
        setDao.insertAllSets(entities)
        Log.d("CardViewModel", "Cached ${entities.size} sets")
    }
}

