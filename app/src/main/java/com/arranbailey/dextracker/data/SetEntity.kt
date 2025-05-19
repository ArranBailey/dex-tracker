package com.arranbailey.dextracker.data

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "sets")
data class SetEntity (
    @PrimaryKey val id: String,
    val name: String,
    val series: String,
    val printedTotal: Int,
    val total: Int,
    val ptcgoCode: String,
    val unlimitedLegal: Boolean,
    val standardLegal: Boolean,
    val expandedLegal: Boolean,
    val releaseDate: String,
    val updatedAt: String,
    val symbolUrl: String,
    val logoUrl: String
)