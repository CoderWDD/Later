package com.example.common.entity

import java.util.*

enum class ItemType {
    POST,
    VIDEO,
    MUSIC,
    WEB_PAGE,
    OTHER
}

data class LaterViewItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val contentUrl: String,
    var thumbnailUrl: String,
    var description: String,
    val tag: List<String>,
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean,
    val isDelete: Boolean,
    val isTop: Boolean,
    val isStar: Boolean = false,
    val folder: String,
    val itemType: ItemType
)
