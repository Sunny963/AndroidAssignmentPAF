package com.example.androidassignmentpaf

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.androidassignmentpaf.databinding.GridItemImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageAdapter(
    private val images: List<Pair<String, Bitmap?>>,
    private val imageCache: ImageCache
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun getItemCount(): Int = images.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            GridItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bitmapImage = images[position]

        /******** Check if image is cached ***********/
        val cachedBitmap = imageCache.getBitmap(bitmapImage.first)
        if (cachedBitmap != null) {
            /********** Use cached image if available **********/
            holder.bind(cachedBitmap)
        } else {
            /********* Load image from URL asynchronously ***********/
            loadImageAsync(holder, bitmapImage)
        }

    }

    private fun loadImageAsync(
        holder: ImageViewHolder,
        bitmap: Pair<String, Bitmap?>
    ) {
        /********* Start a coroutine to load the image asynchronously ******/
        CoroutineScope(Dispatchers.IO).launch {
            imageCache.putBitmap(bitmap.first, bitmap.second)
            /************* Display the image on the UI thread *************/
            withContext(Dispatchers.Main) {
                holder.bind(bitmap.second)
            }
        }
    }

    inner class ImageViewHolder(private val binding: GridItemImageBinding) :
        ViewHolder(binding.root) {
        fun bind(bitmap: Bitmap?) {
            binding.imageView.apply {
                setImageBitmap(bitmap).takeIf { bitmap != null } ?: setImageResource(
                    R.drawable.ic_image_placeholder
                )
            }
        }
    }
}
