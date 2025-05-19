package com.arranbailey.dextracker.model

import androidx.room.PrimaryKey
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.data.SetEntity


fun Card.toEntity(): CardEntity = CardEntity(
    id = id,
    name = name,
    imageUrl = images.small,
    rarity = rarity,
    setName = setName
)

fun CardEntity.toCard(): Card = Card(
    id = id,
    name = name,
    images = CardImages(imageUrl, imageUrl),
    rarity = rarity,
    setName = setName
)

fun CardSet.toSetEntity(): SetEntity = SetEntity(
    id = id,
    name = name,
    series = series,
    printedTotal = printedTotal,
    total = total,
    ptcgoCode = ptcgoCode?:"",
    releaseDate = releaseDate,
    symbolUrl = images.symbol.toString(),
    logoUrl = images.logo.toString(),
    unlimitedLegal = legalities.unlimited.toBoolean(),
    standardLegal = legalities.standard.toBoolean(),
    expandedLegal = legalities.expanded.toBoolean(),
    updatedAt = updatedAt
)

fun SetEntity.toCardSet(): CardSet = CardSet(
    id = id,
    name = name,
    series = series,
    printedTotal = printedTotal,
    total = total,
    ptcgoCode = ptcgoCode,
    releaseDate = releaseDate,
    images = ImagesData(symbolUrl, logoUrl),
    legalities = LegalitiesData(unlimitedLegal.toString(), standardLegal.toString(), expandedLegal.toString()),
    updatedAt = updatedAt
)
