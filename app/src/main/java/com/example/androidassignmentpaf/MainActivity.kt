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
        // TODO by default UI will be render in jetpack compose view
        //  to run using XML view please pass false in loadImages as loadImages(false)
        loadImages(true)
    }

    private fun loadImages(isComposeView: Boolean) {
        binding.apply {
            CoroutineScope(Dispatchers.IO).launch {
                val images = UnsplashService.getImages(
                    pageCount,
                    imageCountPerPage
                )
                CoroutineScope(Dispatchers.Main).launch {
                    if (isComposeView) {
                        composeView.apply {
                            setContent {
                                ImageGrid(images = images)
                            }
                        }
                    } else
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

