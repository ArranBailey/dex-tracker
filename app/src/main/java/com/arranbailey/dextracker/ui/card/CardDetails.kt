package com.arranbailey.dextracker.ui.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.viewmodel.CardDetailsViewModel
import kotlinx.coroutines.flow.map
import androidx.compose.ui.unit.dp

@Composable
fun CardDetailsScreen(viewModel: CardDetailsViewModel = viewModel()) {
    val card = viewModel.card.collectAsState(initial = null)

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        AsyncImage(model = card.value?.imageLarge,
            contentDescription = card.value?.name,
            modifier = Modifier.size(300.dp).padding(6.dp))
    }
}