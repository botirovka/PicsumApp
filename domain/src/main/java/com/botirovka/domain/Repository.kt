package com.botirovka.domain

import android.graphics.Bitmap

interface Repository {
    suspend fun fetchImageBitmap(imageUrl: String): Bitmap?
}