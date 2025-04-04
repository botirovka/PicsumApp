package com.botirovka.picsumapp.ui.imageViewer

import android.graphics.Matrix
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import com.botirovka.picsumapp.utils.ScreenUtils

class GestureListener(private val matrix: Matrix, private val imageView: ImageView) : GestureDetector.SimpleOnGestureListener() {
    private val matrixValues = FloatArray(9)

    override fun onDoubleTap(e: MotionEvent): Boolean {
        matrix.getValues(matrixValues)
        val matrixScale = matrixValues[Matrix.MSCALE_X]

        if (matrixScale > 1 || matrixScale < 1) matrix.reset()
        else matrix.postScale(2f, 2f, e.x, e.y)

        imageView.imageMatrix = matrix
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        matrix.reset()
        imageView.imageMatrix = matrix
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        matrix.getValues(matrixValues)
        val screenWidth = ScreenUtils.getScreenWidthPixels()
        var dX = -distanceX
        var dY = -distanceY
        val translateX = matrixValues[Matrix.MTRANS_X]
        val translateY = matrixValues[Matrix.MTRANS_Y]
        val matrixScale = matrixValues[Matrix.MSCALE_X]
        Log.d("mydebug", "iv width: ${imageView.width} height: ${imageView.height}")
        Log.d("mydebug", "TranslateX: $translateX TranslateY: $translateY")
        if(translateX - distanceX < screenWidth - (imageView.width * matrixScale) || translateX - distanceX > 0){
            dX = 0F
        }
        if(translateY - distanceY < screenWidth - (imageView.width * matrixScale) || translateY - distanceY > 0){
            dY = 0F
        }
        matrix.postTranslate(dX, dY)
        imageView.imageMatrix = matrix
        return true
    }
}