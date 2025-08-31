package com.arranbailey.dextracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
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

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getCardById(id: String): Flow<CardEntity>


    @Query("DELETE FROM cards")
    suspend fun clearAll()
}

@Dao
interface OwnedCardDao {

    @Upsert
    suspend fun upsert(entity: OwnedCardEntity)

    @Query("""
        UPDATE owned_cards SET quantity = quantity + :delta
        WHERE id = :cardId AND variantKey = :variantKey
    """)
    suspend fun addDelta(cardId: String, variantKey: String, delta: Int): Int
    // returns number of rows updated (0 if none)

    @Query("DELETE FROM owned_cards WHERE id = :cardId AND variantKey = :variantKey")
    suspend fun delete(cardId: String, variantKey: String)

    @Query("SELECT * FROM owned_cards WHERE id = :cardId")
    fun getOwnedForCard(cardId: String): Flow<List<OwnedCardEntity>>

    @Query("SELECT * FROM owned_cards")
    fun getAll(): Flow<List<OwnedCardEntity>>
}