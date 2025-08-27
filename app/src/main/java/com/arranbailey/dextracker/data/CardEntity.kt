package com.arranbailey.dextracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageSmall: String,
    val imageLarge: String,
    val rarity: String?,
    val setName: String,
    val setId: String
)

@Entity(tableName = "owned_cards")
data class OwnedCardEntity(
    @PrimaryKey val id: String,
    @PrimaryKey val variantKey: String,
    val quantity: Int)
