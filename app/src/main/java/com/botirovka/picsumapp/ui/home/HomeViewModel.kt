package com.botirovka.picsumapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.botirovka.picsumapp.data.RepositoryImpl
import com.botirovka.picsumapp.domain.models.ImageBitmap
import com.botirovka.picsumapp.domain.models.ImageResult
import com.botirovka.picsumapp.domain.models.PossibleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private var currentId: Int = 0
    private val defaultPageSize = 5
    val imageList = mutableListOf<ImageBitmap>()

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _imagesBitmap = MutableLiveData<ImageResult>()
    val imagesBitmap: LiveData<ImageResult> get() = _imagesBitmap

    init {
        Log.d("mydebug", "loadImages: INIT")
        loadImages(defaultPageSize)
    }


    fun loadImages(size: Int = defaultPageSize){
        viewModelScope.launch {
            Log.d("mydebug", "loadImages: START")
            _loadingLiveData.value = true
            var images = loadImagesParallel(size)
            Log.d("mydebug", "loadImages: $images")
            images = images.filterNotNull()
            if(images.isNotEmpty()){
                _imagesBitmap.value = ImageResult.Data(images)
            }
            else{
                currentId-=defaultPageSize
                _imagesBitmap.value = ImageResult.Error(PossibleError.EmptyImageListError)
            }
            _loadingLiveData.value = false
            Log.d("mydebug", "loadImages: END")
        }
    }

    private suspend fun loadImagesParallel(size: Int): List<ImageBitmap?>{
        Log.d("mydebug", "loadImages: PARALEL")
        val imgRange = (currentId until currentId+size)
        currentId+=size
        val imagesResult = withContext(Dispatchers.IO) {
        val deferredImages = imgRange.map { imageId ->
            async {
                RepositoryImpl.fetchImageBitmap(imageId)
            }
        }
            deferredImages.awaitAll()
        }
        return imagesResult
    }

    fun clearBitmaps(){
        _imagesBitmap.value = ImageResult.None
    }

}