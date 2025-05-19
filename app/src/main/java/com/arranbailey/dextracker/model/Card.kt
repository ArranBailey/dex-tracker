package com.arranbailey.dextracker.model
import android.R
import com.arranbailey.dextracker.model.CardImages
import com.arranbailey.dextracker.model.CardSet

data class Card(
    val id: String,
    val name: String,
    val images: CardImages,
    val rarity: String?,
    val setName: String?,
)