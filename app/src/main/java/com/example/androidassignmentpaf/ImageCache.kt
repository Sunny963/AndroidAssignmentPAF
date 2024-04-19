package com.example.androidassignmentpaf

import android.graphics.Bitmap
import android.util.LruCache

class ImageCache {
    private val memoryCache: LruCache<String, Bitmap>

    init {
        // Initialize memory cache with a portion of available system memory
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8 // Use 1/8th of the available memory for this memory cache
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // Return the size of the bitmap in kilobytes
                return bitmap.byteCount / 1024
            }
        }
    }

    fun putBitmap(key: String, bitmap: Bitmap?) {
        memoryCache.put(key, bitmap)
    }

    fun getBitmap(key: String): Bitmap? = memoryCache.get(key)

    fun clearCache() {
        memoryCache.evictAll()
    }
}
