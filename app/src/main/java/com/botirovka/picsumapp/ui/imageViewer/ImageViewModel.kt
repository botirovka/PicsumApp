package com.botirovka.picsumapp.ui.imageViewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.botirovka.picsumapp.data.RepositoryImpl
import com.botirovka.picsumapp.domain.models.ImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageViewModel: ViewModel() {
    private val _imageBitmap = MutableLiveData<ImageBitmap>()
    val imageBitmap: LiveData<ImageBitmap> get() = _imageBitmap


    fun loadImageById(imageId: Int){
        viewModelScope.launch(Dispatchers.IO) {
           RepositoryImpl.fetchImageBitmap(imageId).let {
               withContext(Dispatchers.Main){
                   _imageBitmap.value = it
               }
            }
        }
    }
}