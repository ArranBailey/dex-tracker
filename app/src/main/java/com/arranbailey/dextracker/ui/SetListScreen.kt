package com.arranbailey.dextracker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arranbailey.dextracker.viewmodel.SetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetListScreen(viewModel: SetViewModel=viewModel()){
    Surface(modifier = Modifier.fillMaxSize()){
        viewModel.displayGrid()
    }
}