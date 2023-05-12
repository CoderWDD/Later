package com.example.imagepicker.extentions

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.example.imagepicker.entity.Album
import com.example.imagepicker.entity.Image
import java.util.Arrays


fun Context.getImagesFromMediaStore(pageSize: Int, pageNumber: Int, albumId: String? = null): List<Image> {
    val images: MutableList<Image> = mutableListOf()

    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    var selection = (MediaStore.Images.Media.MIME_TYPE + "=? OR "
            + MediaStore.Images.Media.MIME_TYPE + "=?")
    var selectionArgs = arrayOf("image/jpeg", "image/png")

    // 如果提供了相册ID，那么只查询该相册下的图片
    if (albumId != null) {
        selection += " AND " + MediaStore.Images.Media.BUCKET_ID + "=?"
        selectionArgs = Arrays.copyOf(selectionArgs, selectionArgs.size + 1)
        selectionArgs[selectionArgs.size - 1] = albumId
    }

    var sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
    val offset = pageNumber * pageSize
    sortOrder += " LIMIT $pageSize OFFSET $offset"

    this.contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        sortOrder
    ).use { cursor ->
        if (cursor != null) {
            val idColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateAddedColumn: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val dataColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val id: Long = cursor.getLong(idColumn)
                val displayName: String = cursor.getString(displayNameColumn)
                val dateAdded: Long = cursor.getLong(dateAddedColumn)
                val imagePath: String = cursor.getString(dataColumn)
                images.add(Image(id, displayName, dateAdded, imagePath))
            }
        }
    }

    return images
}

fun Context.getAlbumsFromMediaStore(): List<Album> {
    val albums = mutableListOf<Album>()

    val albumsMap = mutableMapOf<String, Album>()

    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    val sortOrder = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} DESC"

    contentResolver.query(contentUri, projection, null, null, sortOrder)?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            val id = cursor.getString(idColumn)
            val displayName = cursor.getString(displayNameColumn)
            val album = albumsMap.getOrDefault(id, Album(id, displayName, 0))
            album.count += 1
            albumsMap.putIfAbsent(id, album)
        }
        cursor.close()
    }
    albums.addAll(albumsMap.values)
    albumsMap.clear()
    return albums
}

fun Context.getImagesForAlbum(albumId: String): List<Image> {
    val images = mutableListOf<Image>()
    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DATA
    )

    val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
    val selectionArgs = arrayOf(albumId)

    contentResolver.query(contentUri, projection, selection, selectionArgs, null)?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val displayName = cursor.getString(displayNameColumn)
            val dateAdded = cursor.getLong(dateAddedColumn)
            val data = cursor.getString(dataColumn)

            val image = Image(id, displayName, dateAdded, data)
            images.add(image)
            Log.e("imagePicker", "getImagesForAlbum: ${image.imagePath}", )
        }
    }

    return images
}
