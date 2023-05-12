package com.example.imagepicker.entity

data class AlbumWithImages(
    val album: Album,
    val images: List<Image>,
    var isExpanded: Boolean = true
)