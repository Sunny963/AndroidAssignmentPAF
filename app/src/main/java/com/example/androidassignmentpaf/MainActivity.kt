package com.example.androidassignmentpaf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidassignmentpaf.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val imageCountPerPage = 30
    private val pageCount = 1
    private lateinit var imageCache: ImageCache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageCache = ImageCache()
        loadImages()
    }

    private fun loadImages() {
        binding.apply {
            CoroutineScope(Dispatchers.IO).launch {
                val images = UnsplashService.getImages(
                    pageCount,
                    imageCountPerPage
                )
                CoroutineScope(Dispatchers.Main).launch {
                    rcvImage.adapter = ImageAdapter(images, imageCache)
                }
            }
        }
    }

    override fun onDestroy() {
        imageCache.clearCache()
        super.onDestroy()
    }
}

