package com.botirovka.picsumapp.domain.models

sealed class ImageResult {
    data object None : ImageResult()
    class Data(val data: List<ImageBitmap>) : ImageResult()
    class Error(val error: PossibleError) : ImageResult()
}