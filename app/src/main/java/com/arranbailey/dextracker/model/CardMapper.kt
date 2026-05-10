package com.arranbailey.dextracker.model

import androidx.room.PrimaryKey
import com.arranbailey.dextracker.data.CardEntity
import com.arranbailey.dextracker.data.SetEntity


fun Card.toEntity(): CardEntity = CardEntity(
    id = id,
    name = name,
    imageSmall = image + "/low.webp",
    imageLarge = image + "/high.webp",
    rarity = rarity,
    setName = set.name,
    setId = set.id
)

fun CardEntity.toCard(): Card = Card(
    id = id,
    name = name,
    image = imageSmall.removeSuffix("/low.webp"),
    rarity = rarity,
    set = SetDetails(setName, setId)
)

fun CardSet.toSetEntity(): SetEntity = SetEntity(
    id = id,
    name = name,
    series = serie?.name ?: "",
    printedTotal = cardCount.official,
    total = cardCount.total,
    ptcgoCode = tcgOnline?:"",
    releaseDate = releaseDate?:"",
    symbolUrl =symbol.toString(),
    logoUrl = logo.toString(),
    standardLegal = legal?.standard ?:false,
    expandedLegal = legal?.expanded?:false
)

fun SetEntity.toCardSet(): CardSet = CardSet(
    id = id,
    name = name,
    serie = SerieData(series),
    cardCount = CardCountData(printedTotal, total),
    tcgOnline = ptcgoCode,
    releaseDate = releaseDate,
    symbol=symbolUrl,
    logo = logoUrl,
    legal = LegalitiesData( standardLegal, expandedLegal),
)
