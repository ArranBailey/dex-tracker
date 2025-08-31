package com.arranbailey.dextracker.repos

import com.arranbailey.dextracker.data.OwnedCardDao
import com.arranbailey.dextracker.data.OwnedCardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepo(private val dao: OwnedCardDao) {

    fun quantitiesForCard(cardId: String): Flow<Map<String, Int>> =
        dao.getOwnedForCard(cardId).map { list ->
            list.associate { it.variantKey to it.quantity }
        }

    fun totalForCard(cardId: String): Flow<Int> =
        dao.getOwnedForCard(cardId).map { list -> list.sumOf { it.quantity } }

    suspend fun add(cardId: String, variantKey: String, qty: Int = 1) {
        val updated = dao.addDelta(cardId, variantKey, qty)
        if (updated == 0) dao.upsert(OwnedCardEntity(cardId, variantKey, qty))
    }

    suspend fun remove(cardId: String, variantKey: String, qty: Int = 1) {
        val updated = dao.addDelta(cardId, variantKey, -qty)
        // optional: if it hit 0, you can delete the row to keep table tidy
        // (requires reading the current value first if you want to be exact)
    }
}