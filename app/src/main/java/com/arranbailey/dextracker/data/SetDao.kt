package com.arranbailey.dextracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arranbailey.dextracker.model.SetResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSets(sets: List<SetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(setEntity: SetEntity)

    @Query("SELECT * FROM sets")
    suspend fun getAll(): List<SetEntity>

    @Query("SELECT * FROM sets ORDER BY releaseDate DESC") // DESC for newest first
    fun getAllSetsOrderedByReleaseDate(): Flow<List<SetEntity>>
}