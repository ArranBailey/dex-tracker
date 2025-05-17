package com.arranbailey.dextracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arranbailey.dextracker.model.SetResponse

@Dao
interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSets(sets: List<SetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(setEntity: SetEntity)

    @Query("SELECT * FROM sets")
    suspend fun getAll(): List<SetEntity>
}