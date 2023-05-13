package com.example.imagepicker.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AlbumItem : Parcelable {
    @Parcelize
    data class Title(
        val title: String,
        var count: Int,
        val id: String,
        val isExpanded: Boolean = false
        ) : AlbumItem()

    @Parcelize
    data class Image(
        val id: String,
        val imagePath: String,
        val displayName: String,
        val dateAdded: Long,
        var isSelect: Boolean = false
    ) : AlbumItem()
}