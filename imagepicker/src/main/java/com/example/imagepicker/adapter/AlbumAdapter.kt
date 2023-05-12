package com.example.imagepicker.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.imagepicker.databinding.AlbumItemBinding
import com.example.imagepicker.entity.AlbumWithImages
import com.example.imagepicker.entity.Image

class AlbumAdapter(
    private val context: Context,
    private val albumsWithImages: List<AlbumWithImages>,
    private val onImageSelected: (Image, Boolean) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    companion object {
        val imageRecycledViewPool: RecycledViewPool = RecycledViewPool()
    }


    init {
        imageRecycledViewPool.setMaxRecycledViews(ViewItemType.Image.ordinal, 20)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val viewBinding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val albumWithImages = albumsWithImages[position]
        holder.bind(albumWithImages)
    }

    override fun getItemCount(): Int = albumsWithImages.size

    inner class AlbumViewHolder(viewBinding: AlbumItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        private val albumName: TextView = viewBinding.albumName
        private val imageRecyclerView: RecyclerView = viewBinding.imageRecyclerView

        private val albumContainer: LinearLayoutCompat = viewBinding.albumContainer

        fun bind(albumWithImages: AlbumWithImages) {
            albumName.text = albumWithImages.album.displayName

            val imageAdapter = ImageAdapter(context, albumWithImages.images, onImageSelected)
            imageRecyclerView.setRecycledViewPool(imageRecycledViewPool)
            Log.e("AlbumAdapter", "bind: $imageRecycledViewPool", )
            imageRecyclerView.layoutManager = GridLayoutManager(context, 3)
            imageRecyclerView.adapter = imageAdapter
            imageRecyclerView.visibility = if (albumWithImages.isExpanded) View.VISIBLE else View.GONE

            albumContainer.setOnClickListener {
                albumWithImages.isExpanded = !albumWithImages.isExpanded
                imageRecyclerView.visibility = if (albumWithImages.isExpanded) View.VISIBLE else View.GONE
                notifyDataSetChanged() // 通知数据集改变以刷新视图
            }
        }
    }


}