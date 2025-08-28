package com.arranbailey.dextracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.arranbailey.dextracker.data.CardDatabase

class CardDetailsViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
    val cardId: String = savedStateHandle.get<String>("cardId") ?: ""
    val card = dao.getCardById(cardId)

}