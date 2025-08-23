package com.arranbailey.dextracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arranbailey.dextracker.data.SetEntity
import com.arranbailey.dextracker.viewmodel.SetViewModel
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetListScreen(
    viewModel: SetViewModel = viewModel(),
    onSetClick: (String) -> Unit
){
    val sets = viewModel.sets.collectAsState(initial = emptyList())
    Surface(modifier = Modifier.fillMaxSize()){
        SetGrid(sets = sets.value,onSetClick = { entity -> onSetClick(entity.id)})
    }
}