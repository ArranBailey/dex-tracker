package com.arranbailey.dextracker.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.ui.SetGrid
import com.arranbailey.dextracker.ui.Test
import kotlinx.coroutines.launch

class SetViewModel(application: Application) : AndroidViewModel(application)  {

    val db = CardDatabase.getDatabase(application)
    val dao = db.cardDao()
    val setDao = db.setDao()
    var test = Test()
    var sets = mutableStateOf<List<SetEntity>>(emptyList())

    init {
        viewModelScope.launch {
            getAllSets()
        }
    }

    suspend fun getAllSets() {
        sets.value = setDao.getAll()
    }

    @Composable
    fun displayGrid(){
        test.DisplayGrid(sets.value)

    }

}