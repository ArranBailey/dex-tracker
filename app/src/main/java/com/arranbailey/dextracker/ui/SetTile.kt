package com.arranbailey.dextracker.ui

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.mutableStateOf
import coil.compose.AsyncImage
import com.arranbailey.dextracker.data.CardDatabase
import com.arranbailey.dextracker.data.SetDao
import com.arranbailey.dextracker.data.SetEntity

@Composable
fun SetTile(
    setId: String,
    setName: String,
    cardCount: Int,
    logoUrl: String,
    symbolUrl: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 10.dp)
        ) {
            // Set ID badge (top-left)
            AsyncImage(
                model = symbolUrl,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .height(30.dp)
                    .padding(horizontal = 0.dp, vertical = 0.dp),
                contentScale = ContentScale.Fit
            )
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = "$setName logo",
                    modifier = Modifier
                        .height(82.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = setName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "$cardCount Cards",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



class Test(){

    @Composable
    fun DisplayGrid(sets: List<SetEntity>) {
        SetGrid(sets = sets, onClick = {})
    }
}


@Composable
fun SetGrid(sets: List<SetEntity>, onClick: (SetEntity) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp), // Minimum width per tile
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = sets) { set ->
            SetTile(
                setId = set.id,
                setName = set.name,
                cardCount = set.total,
                symbolUrl = set.symbolUrl,
                logoUrl = set.logoUrl,
                modifier = Modifier
                    .aspectRatio(1f) // 🔲 Makes the tile square
                    .fillMaxWidth()
                    .fillMaxHeight(),
                onClick = { onClick(set) }
            )
        }
    }
}


