package com.arranbailey.dextracker.network

import com.arranbailey.dextracker.model.CardResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("cards")
    suspend fun searchCards(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 50
    ): CardResponse
}
