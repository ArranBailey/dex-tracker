package com.arranbailey.dextracker.ui.card

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.arranbailey.dextracker.viewmodel.CardViewModelOld

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


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
                    CardItem(card)
                }
            }
        }
    }
}

@Composable
fun CardItem(card: Card) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {

        AsyncImage(
            model = card.images.small,
            contentDescription = card.name,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(card.name, fontWeight = FontWeight.Bold)
            Text(card.rarity ?: "Unknown rarity", fontSize = 12.sp)
            Text(card.setName ?: "Unknown set", fontSize = 12.sp)
        }
    }
}