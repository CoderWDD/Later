package com.example.common.entity

enum class ItemType {
    POST,
    VIDEO,
    MUSIC,
    WEB_PAGE,
    OTHER
}

data class LaterViewItem(
    val id: Int,
    val title: String,
    val content: String,
    val contentUrl: String,
    var thumbnailUrl: String,
    var description: String,
    val tag: String,
    val createTime: String,
    val updateTime: String,
    val lastReadTime: String,
    val isRead: Boolean,
    val isDelete: Boolean,
    val isTop: Boolean,
    val isStar: Boolean,
    val folder: String,
    val itemType: ItemType
)
