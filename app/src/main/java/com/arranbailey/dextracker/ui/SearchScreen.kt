package com.arranbailey.dextracker.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arranbailey.dextracker.model.Card
import com.arranbailey.dextracker.ui.card.CardGrid
import com.arranbailey.dextracker.viewmodel.SearchViewModel

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
        //modifier = Modifier.fillMaxWidth(),
        shape = SearchBarDefaults.inputFieldShape,
        colors = SearchBarDefaults.colors(),
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        content = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel(), onClick: (Card) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var cards = viewModel.cards
    Log.d("Info", searchQuery)


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row {
            CustomSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { viewModel.search(searchQuery) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { viewModel.search(searchQuery) }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        CardGrid(cards.value,onClick = onClick)
    }
}