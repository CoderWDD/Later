package com.example.common.entity

data class LaterViewItem(
    val id: Int,
    val title: String,
    val content: String,
    val url: String,
    val tag: String,
    val createTime: String,
    val updateTime: String,
    val lastReadTime: String,
    val isRead: Boolean,
    val isDelete: Boolean,
    val isTop: Boolean,
    val isStar: Boolean,
    val folder: String,
)
