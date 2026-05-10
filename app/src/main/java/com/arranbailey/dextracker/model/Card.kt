package com.arranbailey.dextracker.model
import android.R
//import com.arranbailey.dextracker.model.CardImages
import com.arranbailey.dextracker.model.CardSet

data class SetDetails(
    val name: String,
    val id: String
)

data class Card(
    val id: String,
    val name: String,
    val image: String,
    val rarity: String?,
    val set: SetDetails
)