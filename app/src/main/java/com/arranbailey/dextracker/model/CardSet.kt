package com.arranbailey.dextracker.model

data class CardSet(
    val id: String,
    val name: String,
    val cardCount: CardCountData,
    val tcgOnline: String?,
    val legal: LegalitiesData? =null,
    val releaseDate: String? = null,
    val symbol: String?,
    val logo: String?,
    val serie: SerieData? = null,
    val cards: List<Card>? = null  // add this
)


data class LegalitiesData(
    val standard: Boolean?,
    val expanded: Boolean?
)

data class SerieData(
    val name: String
)

data class CardCountData(
    val official:Int,
    val total: Int
)

