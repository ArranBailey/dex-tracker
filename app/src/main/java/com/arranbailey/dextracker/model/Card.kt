package com.arranbailey.dextracker.model
import com.arranbailey.dextracker.model.CardImages
import com.arranbailey.dextracker.model.CardSet

data class Card(
    val id: String,
    val name: String,
    val images: CardImages,
    val rarity: String?,
    val set: CardSet?
)