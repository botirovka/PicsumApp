package com.botirovka.picsumapp.utils

import android.content.res.Resources


object ScreenUtils {

    fun getScreenWidthPixels(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeightPixels(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun getScreenWidthDp(): Int {
        return (getScreenWidthPixels() / Resources.getSystem().displayMetrics.density).toInt()
    }
}