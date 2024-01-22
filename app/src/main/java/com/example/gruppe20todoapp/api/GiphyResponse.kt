package com.example.gruppe20todoapp.api

data class GiphyResponse(
    val data: GifData
)

data class GifData(
    val images: GifImages
)

data class GifImages(
    val original: GifOriginal
)

data class GifOriginal(
    val url: String
)


