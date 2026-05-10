package com.arranbailey.dextracker.network

import com.arranbailey.dextracker.model.Card
import com.arranbailey.dextracker.model.CardSet
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("cards/{id}")
    suspend fun getCard(@Path("id") cardId: String): Card
    @GET("sets") suspend fun getSets(): List<CardSet>
    @GET("sets/{id}") suspend fun getSetWithCards(@Path("id") setId: String): CardSet }
