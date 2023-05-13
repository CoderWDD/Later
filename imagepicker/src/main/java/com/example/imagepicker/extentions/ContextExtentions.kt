package com.example.imagepicker.extentions

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.imagepicker.entity.Album
import com.example.imagepicker.entity.AlbumItem
import com.example.imagepicker.entity.Image
import java.util.Arrays


fun Context.getImagesFromMediaStore(pageSize: Int, pageNumber: Int, albumId: String? = null): List<AlbumItem.Image> {
    val images: MutableList<AlbumItem.Image> = mutableListOf()
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

    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $pageSize OFFSET ${pageNumber * pageSize}"

    createCursor(
        contentResolver = this.contentResolver,
        collection = uri,
        projection = projection,
        whereCondition = selection,
        selectionArgs = selectionArgs,
        orderBy = MediaStore.Images.Media.DATE_ADDED,
        orderAscending = false,
        limit = pageSize,
        offset = pageNumber * pageSize
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val displayName = cursor.getString(displayNameColumn)
            val dateAdded = cursor.getLong(dateAddedColumn)
            val data = cursor.getString(dataColumn)
            val image = AlbumItem.Image(id = id.toString(), displayName = displayName, dateAdded = dateAdded, imagePath = data)
            images.add(image)
            Log.e("imagePicker", "getImagesForAlbum: ${image.imagePath}", )
        }
    }
    return images
}

fun Context.getAlbumsFromMediaStore(): List<AlbumItem.Title> {
    val albums = mutableListOf<AlbumItem.Title>()

    val albumsMap = mutableMapOf<String, AlbumItem.Title>()

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
            val album = albumsMap.getOrDefault(id, AlbumItem.Title(id = id, title = displayName, count = 0))
            album.count += 1
            albumsMap.putIfAbsent(id, album)
        }
        cursor.close()
    }
    albums.addAll(albumsMap.values)
    albumsMap.clear()
    return albums
}

fun Context.getImagesForAlbum(albumId: String): List<AlbumItem.Image> {
    val images = mutableListOf<AlbumItem.Image>()
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

            val image = AlbumItem.Image(id = id.toString(), displayName = displayName, dateAdded = dateAdded, imagePath = data)
            images.add(image)
            Log.e("imagePicker", "getImagesForAlbum: ${image.imagePath}", )
        }
    }

    return images
}


private fun createCursor(
    contentResolver: ContentResolver,
    collection: Uri,
    projection: Array<String>,
    whereCondition: String,
    selectionArgs: Array<String>,
    orderBy: String,
    orderAscending: Boolean,
    limit: Int = 20,
    offset: Int = 0
): Cursor? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
        val selection = createSelectionBundle(whereCondition, selectionArgs, orderBy, orderAscending, limit, offset)
        contentResolver.query(collection, projection, selection, null)
    }
    else -> {
        val orderDirection = if (orderAscending) "ASC" else "DESC"
        var order = when (orderBy) {
            "ALPHABET" -> "${MediaStore.Audio.Media.TITLE}, ${MediaStore.Audio.Media.ARTIST} $orderDirection"
            else -> "${MediaStore.Audio.Media.DATE_ADDED} $orderDirection"
        }
        order += " LIMIT $limit OFFSET $offset"
        contentResolver.query(collection, projection, whereCondition, selectionArgs, order)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun createSelectionBundle(
    whereCondition: String,
    selectionArgs: Array<String>,
    orderBy: String,
    orderAscending: Boolean,
    limit: Int = 20,
    offset: Int = 0
): Bundle = Bundle().apply {
    // Limit & Offset
    putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
    putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
    // Sort function
    when (orderBy) {
        "ALPHABET" -> putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Files.FileColumns.TITLE))
        else -> putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Files.FileColumns.DATE_ADDED))
    }
    // Sorting direction
    val orderDirection =
        if (orderAscending) ContentResolver.QUERY_SORT_DIRECTION_ASCENDING else ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
    putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, orderDirection)
    // Selection
    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, whereCondition)
    putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs)
}