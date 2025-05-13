package com.arranbailey.dextracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardEntity>)

    @Query("SELECT * FROM cards WHERE name LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<CardEntity>

    @Query("DELETE FROM cards")
    suspend fun clearAll()
}