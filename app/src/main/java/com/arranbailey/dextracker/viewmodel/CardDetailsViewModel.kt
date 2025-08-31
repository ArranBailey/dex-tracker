package com.arranbailey.dextracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.repos.CollectionRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardDetailsViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
    private val repo = CollectionRepo(db.ownedCardDao())
    private val cardId: String = checkNotNull(savedStateHandle["cardId"])
    val card = dao.getCardById(cardId)

    // total owned across all variants (for “Owned: X” / “Not Owned”)
    val total: StateFlow<Int> =
        repo.totalForCard(cardId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // per-variant map if/when you need it
    val variantQuantities: StateFlow<Map<String, Int>> =
        repo.quantitiesForCard(cardId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun addOne(variantKey: String = "Normal") = viewModelScope.launch {
        repo.add(cardId, variantKey, 1)
    }

    fun removeOne(variantKey: String = "Normal") = viewModelScope.launch {
        repo.remove(cardId, variantKey, 1)
    }

}