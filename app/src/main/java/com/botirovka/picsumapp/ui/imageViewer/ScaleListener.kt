package com.botirovka.picsumapp.ui.imageViewer

import android.graphics.Matrix
import android.util.Log
import android.view.ScaleGestureDetector
import android.widget.ImageView
import com.botirovka.picsumapp.utils.ScreenUtils

class ScaleListener(private val matrix: Matrix, private val imageView: ImageView) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private val matrixValues = FloatArray(9)
    private var scaleFactor = 1.0f

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        matrix.getValues(matrixValues)
        val matrixScale = matrixValues[Matrix.MSCALE_X]
        val ivCenter = ScreenUtils.getScreenWidthPixels() / 2.0F
        val translateX = matrixValues[Matrix.MTRANS_X]
        val translateY = matrixValues[Matrix.MTRANS_Y]
        scaleFactor = detector.scaleFactor
        scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)
        Log.d("mydebug", "onScale Matrix: $matrixScale")
        Log.d("mydebug", "onScale Scale: $scaleFactor")
        val focusX = detector.focusX
        val focusY = detector.focusY

        if (scaleFactor > 1 && (matrixScale * scaleFactor) >= 3){
            return true
        }
        if (scaleFactor < 1 && (matrixScale * scaleFactor) <= 0.5){
            return true
        }

        when{
            matrixScale > 1 -> matrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
            matrixScale <= 1 -> matrix.postScale(scaleFactor, scaleFactor,ivCenter,ivCenter)
        }

        imageView.imageMatrix = matrix

        return true
    }
}