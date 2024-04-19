package com.example.androidassignmentpaf

import android.graphics.Bitmap
import android.util.Log
import org.json.JSONArray

object UnsplashService {
    private const val BASE_URL = "https://api.unsplash.com/photos"
    private const val ACCESS_KEY = "lVRIwXQVgSBKc3NipilYt2fihH0KQOay5ZLYxU_-vNA"

    fun getImages(page: Int, perPage: Int): List<Pair<String, Bitmap?>> =
        try {
            BASE_URL.httpResponse(page, perPage, ACCESS_KEY).parseResponse().map {
                Pair(it, it.bitmap())
            }
        } catch (e: Exception) {
            listOf()
        }
}


private fun String.parseResponse(): List<String> {
    val images = mutableListOf<String>()
    try {
        JSONArray(this).apply {
            for (i in 0 until length()) {
                images.add(getJSONObject(i).getJSONObject("urls").getString("regular"))
            }
        }
    } catch (e: Exception) {
        Log.e("UnsplashService", "Parsing Error: ${e.message}")
    }
    return images
}
