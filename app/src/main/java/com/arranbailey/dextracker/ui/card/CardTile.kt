package com.arranbailey.dextracker.ui.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arranbailey.dextracker.model.Card

@Composable
fun CardTile(card: Card) {
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
            Text(card.set.name, fontSize = 12.sp)
        }
    }
}