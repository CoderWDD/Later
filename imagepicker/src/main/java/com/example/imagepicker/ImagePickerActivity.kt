package com.example.imagepicker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.adapter.AlbumAdapter
import com.example.imagepicker.entity.AlbumWithImages
import com.example.imagepicker.extentions.getAlbumsFromMediaStore
import com.example.imagepicker.extentions.getImagesForAlbum

class ImagePickerActivity : AppCompatActivity() {

    private lateinit var albumRecyclerView: RecyclerView
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)

        albumRecyclerView = findViewById(R.id.album_recycler_view)

        checkAndRequestPermissions()
    }

    private val REQUEST_READ_EXTERNAL_STORAGE = 1

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            loadImages()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages()
                } else {
                    Toast.makeText(this, "Permission denied. Please grant storage access.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadImages() {
        val albums = getAlbumsFromMediaStore()
        val albumsWithImages = albums.map { album ->
            val images = getImagesForAlbum(album.id)
            AlbumWithImages(album, images)
        }

        albumAdapter = AlbumAdapter(this, albumsWithImages) { image, isSelected ->
            // Handle image selection
            if (isSelected) {
                Toast.makeText(this, "Selected: ${image.displayName}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Deselected: ${image.displayName}", Toast.LENGTH_SHORT).show()
            }
        }

        albumRecyclerView.layoutManager = LinearLayoutManager(this)
//        concatAdapter = ConcatAdapter(albumTitleAdapter, imageGridAdapter)

        albumRecyclerView.adapter = albumAdapter
    }
}