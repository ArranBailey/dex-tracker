package com.arranbailey.dextracker.model

data class CardSet(
    val id: String,
    val name: String,
    val series: String,
    val printedTotal: Int,
    val total: Int,
    val ptcgoCode: String?,
    val legalities: LegalitiesData,
    val releaseDate: String,
    val updatedAt: String,
    val images: ImagesData
)


data class LegalitiesData(
    val unlimited: String,
    val standard: String?, // Based on your CardSet, standard/expandedLegal are booleans
    val expanded: String?  // You'll need to map "Legal" string to Boolean true
)

data class ImagesData(
    val symbol: String?, // Make nullable if they can ever be missing
    val logo: String?    // Make nullable if they can ever be missing
)
