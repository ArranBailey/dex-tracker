package com.arranbailey.dextracker.ui.card

import android.R.attr.onClick
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arranbailey.dextracker.model.Card
import com.arranbailey.dextracker.model.toCard
import com.arranbailey.dextracker.viewmodel.CardViewModel
import com.arranbailey.dextracker.viewmodel.CardViewModelOld
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch()
                    isExpanded = false
                },
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                placeholder = { Text("Search Pokémon") }
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = Modifier.fillMaxWidth(),
        shape = SearchBarDefaults.inputFieldShape,
        colors = SearchBarDefaults.colors(),
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSearchScreen(viewModel: CardViewModelOld = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    Log.d("Info", searchQuery)
    val isCaching = viewModel.isCaching


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (isCaching.value) {
                Text("Caching cards...", Modifier.padding(16.dp))
        }
        CustomSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { viewModel.search(searchQuery) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.cacheSet("\"$searchQuery\"") }) {
            Text("Cache Platinum Set")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.loadCardsFromCache(searchQuery) }) {
            Text("Load Cached Set")
        }
        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(viewModel.cards.value) { card ->
                    CardItem(card, onClick = {card -> card.id})
                }
            }
        }
    }
}

@Composable
fun CardListScreen(viewModel: CardViewModel = viewModel(),
                   onClick: (Card) -> Unit) {
    val cards by viewModel.cards.collectAsState(initial = emptyList())
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()){
        items(items = cards) { card ->
            CardItem(card.toCard(), onClick = onClick)
        }
    }
}


@Composable
fun CardItem(card: Card, onClick: (card: Card) -> Unit) {
    AsyncImage(
        model = card.images.large,
        contentDescription = card.name,
        modifier = Modifier.fillMaxWidth().clickable{ onClick(card) }
    )
}