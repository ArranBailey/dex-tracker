package com.arranbailey.dextracker.ui.card

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.viewmodel.CardDetailsViewModel
import kotlinx.coroutines.flow.map
import androidx.compose.ui.unit.dp

@Composable
fun CardDetailsScreen(viewModel: CardDetailsViewModel = viewModel()) {
    val card = viewModel.card.collectAsState(initial = CardEntity("0","Error","Error","Error","Error","Error","Error"))
    val quantity = viewModel.total.collectAsState(initial = 0)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = card.value.imageLarge,
            contentDescription = card.value.name,
            modifier = Modifier
                .size(340.dp)
                .padding(15.dp)
        )

        Text(card.value.name, fontWeight = FontWeight.Bold)
        Text(card.value.setName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // Minus
            androidx.compose.material3.Button(
                onClick = {viewModel.removeOne()},
                enabled = true,
                shape = RoundedCornerShape(
                    topStart = 8.dp, bottomStart = 8.dp,
                    topEnd = 0.dp, bottomEnd = 0.dp
                ),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text("-")
            }

            // Middle status
            OutlinedButton(
                onClick = {},
                shape = RectangleShape, // flat sides
                modifier = Modifier.weight(2f).height(48.dp) // bigger space
            ) {
                Text(if (quantity.value == 0) "Not Owned" else "Owned: ${quantity.value}")
            }

            // Plus
            androidx.compose.material3.Button(
                onClick = { viewModel.addOne() },
                shape = RoundedCornerShape(
                    topStart = 0.dp, bottomStart = 0.dp,
                    topEnd = 8.dp, bottomEnd = 8.dp
                ),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text("+")
            }
        }
    }
}