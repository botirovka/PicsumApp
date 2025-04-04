package com.botirovka.picsumapp.utils

import android.content.Intent
import androidx.fragment.app.Fragment
import com.botirovka.picsumapp.domain.models.ImageBitmap

object Extenstions {
    fun Fragment.onShareClick(imageBitmap: ImageBitmap) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Image from PicsumApp:\n ${imageBitmap.url}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}