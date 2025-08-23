package com.arranbailey.dextracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardEntity>)

    @Query("SELECT * FROM cards WHERE name LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<CardEntity>

    @Query("SELECT * FROM cards WHERE setId = :setId")
    fun getCardsBySet(setId: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE setName = :setName")
    fun getCardsBySetName(setName: String): List<CardEntity>


    @Query("DELETE FROM cards")
    suspend fun clearAll()
}