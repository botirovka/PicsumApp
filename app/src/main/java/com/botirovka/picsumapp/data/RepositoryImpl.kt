package com.botirovka.picsumapp.data

import android.graphics.BitmapFactory
import android.util.Log
import com.botirovka.picsumapp.domain.models.ImageBitmap
import com.botirovka.picsumapp.domain.models.Repository
import com.botirovka.picsumapp.utils.ScreenUtils
import java.net.URL

object RepositoryImpl: Repository{
    private val imageCache = mutableMapOf<Int, ImageBitmap>()
    override var baseUrl: String =  "https://picsum.photos/"
    override val screenWidth: Int = ScreenUtils.getScreenWidthPixels()


    override suspend fun fetchImageBitmap(imageId: Int): ImageBitmap? {
        Log.d("mydebug", "loadImages: FETCH")
        return imageCache[imageId] ?: try {
            Log.d("mydebug", "loadImages: FETCH TRY")
            val url = URL("$baseUrl/id/$imageId/$screenWidth")
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            ImageBitmap(imageId, url, bitmap).also {
                imageCache[imageId] = it
                Log.d("mydebug", "Cache: $imageCache")
            }
        } catch (e: Exception) {
            Log.d("mydebug", "Exception $e")
            null
        }
    }

    fun getCachedImage(imageId: Int): ImageBitmap? {
        return imageCache[imageId]
    }
}