package com.example.gruppe20todoapp.utility

import com.example.gruppe20todoapp.api.GiphyApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// In RetrofitInstance.kt
object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val giphyApiService: GiphyApiService by lazy {
        retrofit.create(GiphyApiService::class.java)
    }
}
