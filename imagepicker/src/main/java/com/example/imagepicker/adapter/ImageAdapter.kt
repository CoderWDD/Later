package com.example.imagepicker.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.imagepicker.databinding.ImageItemBinding
import com.example.imagepicker.entity.Image

class ImageAdapter(
    private val context: Context,
    private val images: List<Image>,
    private val onImageSelected: (Image, Boolean) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    companion object{
        var count = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        count++
        Log.e("ImageAdapter", "onCreateViewHolder: $count", )
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        Log.e("ImageAdapter", "onCreateViewHolder: $viewType", )
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(private val itemBinding: ImageItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private val imageView: ImageView = itemBinding.imageView
        private val checkBox: CheckBox = itemBinding.checkBox

        fun bind(image: Image) {
            Glide.with(context)
                .load(image.imagePath)
                .sizeMultiplier(0.2f)
                .into(imageView)

            checkBox.isChecked = image.isSelect

            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                image.isSelect = checkBox.isChecked
                onImageSelected(image, checkBox.isChecked)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = ViewItemType.Image.ordinal
}