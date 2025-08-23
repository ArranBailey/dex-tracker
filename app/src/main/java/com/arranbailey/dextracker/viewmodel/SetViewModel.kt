package com.arranbailey.dextracker.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.ui.SetGrid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetViewModel(application: Application) : AndroidViewModel(application)  {

    val db = CardDatabase.getDatabase(application)
    val setDao = db.setDao()
    val sets = setDao.getAllSetsOrderedByReleaseDate()

    init {
        viewModelScope.launch {
            getAllSets()
        }
    }

    suspend fun getAllSets() {
        //sets.value = setDao.getAllSetsOrderedByReleaseDate()
    }


}