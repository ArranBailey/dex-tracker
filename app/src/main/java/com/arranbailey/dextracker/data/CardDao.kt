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

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getCardById(id: String): Flow<CardEntity>


    @Query("DELETE FROM cards")
    suspend fun clearAll()
}

@Dao
interface OwnedCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCard(ownedCardEntity: OwnedCardEntity)

    @Query("SELECT * FROM owned_cards WHERE id = :id AND variantKey = :variantKey")
    fun getOwnedCard(id: String, variantKey: String): OwnedCardEntity

    @Query("DELETE FROM owned_cards WHERE id = :id AND variantKey = :variantKey")
    fun removeCard(id: String, variantKey: String)

    @Query("SELECT * FROM owned_cards WHERE id = :id")
    fun getOwnedCards(id: String): List<OwnedCardEntity>

}