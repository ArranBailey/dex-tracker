package com.arranbailey.dextracker.network

import com.arranbailey.dextracker.model.CardResponse
import com.arranbailey.dextracker.model.SetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("cards")
    suspend fun searchCards(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 50
    ): CardResponse

    @GET("cards")
    suspend fun getCardsBySet(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 250
    ): CardResponse

    @GET("sets")
    suspend fun searchSets(
        @Query("q") query: String,
    ): SetResponse
}
