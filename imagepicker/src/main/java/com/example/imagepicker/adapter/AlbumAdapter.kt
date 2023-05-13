package com.example.imagepicker.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imagepicker.R
import com.example.imagepicker.databinding.AlbumItemBinding
import com.example.imagepicker.databinding.ImageItemBinding
import com.example.imagepicker.entity.AlbumItem

class AlbumAdapter(
    private val context: Context,
    private val onAlbumClicked: (AlbumItem.Title) -> Unit,
    private val onImageClicked: (AlbumItem.Image) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val albums: MutableList<AlbumItem> = mutableListOf()

    private var page = 0
    private val pageSize = 20

    private val _selectedImages: MutableLiveData<List<AlbumItem.Image>> = MutableLiveData()

    val selectedImages: LiveData<List<AlbumItem.Image>> = _selectedImages

    private var onAlbumTitleClickListener: OnAlbumTitleClickListener? = null

    private var onLoadMoreListener: OnLoadMoreListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ViewItemType.Image.ordinal -> {
                val viewBinding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageItemViewHolder(viewBinding)
            }
            ViewItemType.Title.ordinal -> {
                val viewBinding = AlbumItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AlbumTitleViewHolder(viewBinding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = albums[position]
        when (holder) {
            is AlbumTitleViewHolder -> {
                album as AlbumItem.Title
                holder.apply {
                    albumName.text = album.title

                    albumView.setOnClickListener {
                        onAlbumTitleClickListener?.onAlbumTitleClick(position)
                    }

                    if (album.isExpanded) {
                        albumName.setTypeface(albumName.typeface, Typeface.BOLD)
                    } else {
                        albumName.setTypeface(albumName.typeface, Typeface.NORMAL)
                    }

                    itemView.setOnClickListener {
                        onAlbumClicked(album)
                    }
                }
            }
            is ImageItemViewHolder -> {
                album as AlbumItem.Image

                // 保证图片是正方形
                holder.imageView.post {
                    val width = holder.imageView.width
                    val params = holder.imageView.layoutParams
                    params.height = width
                    holder.imageView.layoutParams = params
                }

                Glide.with(context)
                    .load(album.imagePath)
                    .override(holder.imageView.width, holder.imageView.width)
                    .thumbnail(0.2f)
                    .placeholder(R.drawable.baseline_placeholder_24)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView)

                holder.apply {
                    // 取消监听器，以防止触发不必要的事件
                    checkBox.setOnCheckedChangeListener(null)
                    itemView.setOnClickListener(null)

                    // 根据图片的选中状态设置选择框的状态
                    checkBox.isChecked = album.isSelect

                    // 整个图片的点击事件
                    itemView.setOnClickListener {
                        onImageClicked(album)
                    }

                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        album.isSelect = isChecked
                        if (isChecked) {
                            _selectedImages.value = _selectedImages.value?.plus(album) ?: listOf(album)
                        } else {
                            _selectedImages.value = _selectedImages.value?.minus(album) ?: listOf()
                        }
                    }
                }
            }
            else -> throw IllegalArgumentException("Invalid view holder")
        }
    }

    override fun getItemCount(): Int = albums.size

    override fun getItemViewType(position: Int): Int {
        return when (albums[position]) {
            is AlbumItem.Title -> ViewItemType.Title.ordinal
            is AlbumItem.Image -> ViewItemType.Image.ordinal
        }
    }

    fun setOnAlbumTitleClickListener(onAlbumTitleClickListener: OnAlbumTitleClickListener) {
        this.onAlbumTitleClickListener = onAlbumTitleClickListener
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun loadMore(){
        page++
        onLoadMoreListener?.onLoadMore(page = page, pageSize = pageSize)
    }

    inner class AlbumTitleViewHolder(viewBinding: AlbumItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val albumName: TextView = viewBinding.albumName
        val albumView = viewBinding.root
    }

    inner class ImageItemViewHolder(itemBinding: ImageItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val imageView: ImageView = itemBinding.imageView
        val checkBox: CheckBox = itemBinding.checkBox
    }

    interface OnAlbumTitleClickListener {
        fun onAlbumTitleClick(position: Int)
    }

    interface OnLoadMoreListener {
        fun onLoadMore(page: Int, pageSize: Int)
    }

    fun addAlbums(albums: List<AlbumItem>) {
        this.albums.addAll(albums)
        notifyItemRangeInserted(this.albums.size - albums.size, albums.size)
    }

    fun addAlbum(album: AlbumItem) {
        this.albums.add(album)
        notifyItemInserted(this.albums.size - 1)
    }

}