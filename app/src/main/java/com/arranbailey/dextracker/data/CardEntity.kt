package com.arranbailey.dextracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    val rarity: String?,
    val setName: String?
)