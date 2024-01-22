package com.example.gruppe20todoapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {
    @GET("gifs/random")
    suspend fun getRandomGif(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String
    ): GiphyResponse
}
