package com.arranbailey.dextracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.CardEntity
import kotlin.toString

class CardViewModel(application: Application, savedStateHandle: SavedStateHandle): AndroidViewModel(application) {
    private val db = CardDatabase.getDatabase(application)
    private val dao = db.cardDao()
    val setId: String = savedStateHandle.get<String>("setId") ?: ""
    val cards = dao.getCardsBySet(setId)

    init {
        Log.d("setID", setId.toString())
    }


}