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

@Entity(primaryKeys = ["id", "variantKey"], tableName = "owned_cards")
data class OwnedCardEntity(
    val id: String,
    val variantKey: String,
    val quantity: Int)
