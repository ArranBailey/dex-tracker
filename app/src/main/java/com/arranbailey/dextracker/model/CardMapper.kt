package com.arranbailey.dextracker.model

import androidx.room.PrimaryKey
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.data.SetEntity


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
    set = CardSet(id=id ,name = setName ?: "Unknown", series = "")
)

fun CardSet.toSetEntity(): SetEntity = SetEntity(
    id = id,
    name = name,
    series = series
)

fun SetEntity.toCardSet(): CardSet = CardSet(
    id = id,
    name = name,
    series = series
)
