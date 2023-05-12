package com.example.imagepicker.entity

data class Image(
    val id: Long,
    val displayName: String,
    val dateAdded: Long,
    val imagePath: String,
    var isSelect: Boolean = false
)