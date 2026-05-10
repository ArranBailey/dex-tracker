package com.arranbailey.dextracker.network

//import com.arranbailey.dextracker.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS


object RetrofitInstance {
   // private const val BASE_URL = "https://api.pokemontcg.io/v2/"
    private const val BASE_URL = "https://api.tcgdex.net/v2/en/"
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

//    val authInterceptor = Interceptor { chain ->
//        val requestWithApiKey =
//            chain.request()
//                .newBuilder()
//                .header("X-Api-Key", BuildConfig.POKEMON_API_KEY)
//                .build()
//        chain.proceed(requestWithApiKey)
//    }

    //For slow server
//    val client = OkHttpClient.Builder()
//        .addInterceptor(authInterceptor)
//        .addInterceptor(logging)
//        .connectTimeout(300, SECONDS)
//        .readTimeout(350, SECONDS)
//        .writeTimeout(350, SECONDS)
//        .callTimeout(300, SECONDS)
//        .retryOnConnectionFailure(true)
//        .build()

    val client = OkHttpClient.Builder()
//        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .connectTimeout(10, SECONDS)
        .readTimeout(10, SECONDS)
        .writeTimeout(10, SECONDS)
        .callTimeout(10, SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PokeApiService::class.java)
    }
}