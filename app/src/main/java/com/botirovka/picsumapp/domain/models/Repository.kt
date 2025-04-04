package com.botirovka.picsumapp.domain.models

interface Repository {
    var baseUrl: String
    val screenWidth: Int
    suspend fun fetchImageBitmap(imageId: Int): ImageBitmap?
}