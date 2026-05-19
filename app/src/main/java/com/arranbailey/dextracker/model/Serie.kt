package com.arranbailey.dextracker.model

data class Serie(
    val id: String,
    val name: String,
    val logo: String? = null
)

data class SetBrief(
    val id: String,
    val name: String,
    val cardCount: CardCountData? = null,
    val logo: String? = null,
    val symbol: String? = null
)

data class SerieWithSets(
    val id: String,
    val name: String,
    val logo: String? = null,
    val sets: List<SetBrief>? = null
)