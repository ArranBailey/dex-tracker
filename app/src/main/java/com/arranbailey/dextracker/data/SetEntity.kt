package com.arranbailey.dextracker.data

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "sets")
data class SetEntity (
    @PrimaryKey val id: String,
    val name: String,
    val series: String
)