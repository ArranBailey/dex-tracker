package com.arranbailey.dextracker.model

import com.arranbailey.dextracker.data.CardEntity

fun Card.toEntity(): CardEntity = CardEntity(
    id = id,
    name = name,
    imageUrl = images.small,
    rarity = rarity,
    setName = set?.name
)

fun CardEntity.toCard(): Card = Card(
    id = id,
    name = name,
    images = CardImages(imageUrl, imageUrl),
    rarity = rarity,
    set = CardSet(name = setName ?: "Unknown", series = "")
)