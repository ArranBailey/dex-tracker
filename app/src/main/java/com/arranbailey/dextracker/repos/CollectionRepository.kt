package com.arranbailey.dextracker.repos

import androidx.compose.runtime.collectAsState
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

    suspend fun remove(cardId: String, variantKey: String = "Normal", qty: Int = 1) {
        var updated = 0
        val quantity = dao.getQuantityForCard(cardId, variantKey)
        if (quantity != null) {
            if (quantity == 1 ) dao.delete(cardId, variantKey)
            else if (quantity > 1)  updated = dao.addDelta(cardId, variantKey, -qty)
        }
    }
}