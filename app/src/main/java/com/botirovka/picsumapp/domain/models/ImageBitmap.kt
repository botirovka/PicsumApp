package com.botirovka.picsumapp.domain.models

import android.graphics.Bitmap
import java.net.URL

data class ImageBitmap(
    val id: Int,
    val url: URL,
    val bitmap: Bitmap?
)

