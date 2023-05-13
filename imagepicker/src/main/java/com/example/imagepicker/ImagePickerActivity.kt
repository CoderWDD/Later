package com.example.imagepicker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.adapter.AlbumAdapter
import com.example.imagepicker.adapter.ViewItemType
import com.example.imagepicker.constants.ResultCode
import com.example.imagepicker.constants.ResultData
import com.example.imagepicker.databinding.ActivityImagePickerBinding
import com.example.imagepicker.entity.AlbumItem
import com.example.imagepicker.extentions.getImagesFromMediaStore
import java.util.ArrayList
import java.util.zip.Inflater

class ImagePickerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityImagePickerBinding
    private lateinit var albumRecyclerView: RecyclerView
    private lateinit var albumAdapter: AlbumAdapter
    private val selectedImages: MutableList<AlbumItem.Image> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initRecyclerView()
        initToolbar()
        checkAndRequestPermissions()
    }

    private fun initToolbar(){
        setSupportActionBar(viewBinding.toolbarImagePicker.toolbarImagePicker)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        viewBinding.toolbarImagePicker.toolbarImagePicker.setNavigationOnClickListener {
            finish()
        }

        viewBinding.toolbarImagePicker.btnDone.setOnClickListener {
            // 如果能触发这个事件，说明一定选择了至少一张图片
            val resultIntent = Intent()
            resultIntent.putParcelableArrayListExtra(ResultData.SELECTED_IMAGES, ArrayList<AlbumItem.Image>(selectedImages))
            setResult(ResultCode.Image.OK, resultIntent)
            finish()
        }
    }

    // Update the selected count TextView whenever the selection changes
    private fun updateSelectedCount(count: Int) {
        // 如果 count 大于0, 则显示，且改变 button 的背景色
        if (count > 0) {
            viewBinding.toolbarImagePicker.btnDone.isEnabled = true
            viewBinding.toolbarImagePicker.btnDone.text = "${getString(R.string.done)}($count)"
            viewBinding.toolbarImagePicker.btnDone.setTextColor(resources.getColor(R.color.md_theme_light_onSecondary))
            viewBinding.toolbarImagePicker.btnDone.setBackgroundResource(R.drawable.button_active_bg)
        } else {
            viewBinding.toolbarImagePicker.btnDone.isEnabled = false
            viewBinding.toolbarImagePicker.btnDone.text = getString(R.string.done)
            viewBinding.toolbarImagePicker.btnDone.setTextColor(resources.getColor(R.color.md_theme_light_onSurfaceVariant))
            viewBinding.toolbarImagePicker.btnDone.setBackgroundResource(R.drawable.button_inactive_bg)
        }
    }

    private fun initRecyclerView(){
        val spanCount = 3
        albumAdapter = AlbumAdapter(context = this, onAlbumClicked = { album ->

        }, onImageClicked = { image ->

        })

        albumRecyclerView = findViewById(R.id.album_recycler_view)
        albumRecyclerView.adapter = albumAdapter

        val gridLayoutManager = GridLayoutManager(this, spanCount)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (albumAdapter.getItemViewType(position)) {
                    ViewItemType.Image.ordinal -> 1
                    ViewItemType.Title.ordinal -> spanCount
                    else -> throw IllegalArgumentException("Invalid view type")
                }
            }
        }

        albumRecyclerView.layoutManager = gridLayoutManager

        albumRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = gridLayoutManager.itemCount
                val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()
                // 如果剩余项目少于阈值，则触发onLoadMore回调
                if (totalItemCount - lastVisibleItemPosition < 6) {
                    albumAdapter.loadMore()
                }
            }
        })

        albumAdapter.setOnLoadMoreListener(object : AlbumAdapter.OnLoadMoreListener {
            override fun onLoadMore(page: Int, pageSize: Int) {
                val imagesFromMediaStore = getImagesFromMediaStore(pageSize = pageSize, pageNumber = page)
                albumAdapter.addAlbums(imagesFromMediaStore)
            }
        })

        albumAdapter.selectedImages.observe(this) { selectedImages ->
            this.selectedImages.clear()
            this.selectedImages.addAll(selectedImages)
            updateSelectedCount(selectedImages.size)
        }
    }

    private val REQUEST_READ_EXTERNAL_STORAGE = 1

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            loadImages()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages()
                } else {
                    Toast.makeText(
                        this,
                        "Permission denied. Please grant storage access.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun loadImages() {
        val albums = getImagesFromMediaStore(pageNumber = 0, pageSize = 20)
        albumAdapter.addAlbums(albums)
    }
}