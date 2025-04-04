package com.botirovka.picsumapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.botirovka.picsumapp.data.RepositoryImpl
import com.botirovka.picsumapp.domain.models.ImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private var currentId: Int = 0
    private val defaultPageSize = 5

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    private val _imagesBitmap = MutableLiveData<List<ImageBitmap>>()
    val imagesBitmap: LiveData<List<ImageBitmap>> get() = _imagesBitmap

    init {
        loadImages(defaultPageSize)
    }


    fun loadImages(size: Int = defaultPageSize){
        viewModelScope.launch {
            _loadingLiveData.value = true
            val images = loadImagesParallel(size)
            Log.d("mydebug", "loadImages: $images")
            if(_imagesBitmap.value?.size == null){
                _imagesBitmap.value = images.filterNotNull()
            }
            else{
                _imagesBitmap.value = _imagesBitmap.value?.plus(images.filterNotNull())
            }
            _loadingLiveData.value = false
        }
    }

    private suspend fun loadImagesParallel(size: Int): List<ImageBitmap?>{
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

}