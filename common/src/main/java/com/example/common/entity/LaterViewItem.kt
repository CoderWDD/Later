package com.example.common.entity

import java.util.*

enum class ItemType {
    POST,
    VIDEO,
    MUSIC,
    WEB_PAGE,
    OTHER
}

data class LaterViewItem @JvmOverloads constructor(
    var key: String = "",
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val contentUrl: String = "",
    var thumbnailUrl: String = "",
    var description: String = "",
    val tag: List<String> = emptyList(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val lastReadTime: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val isDelete: Boolean = false,
    val isTop: Boolean = false,
    val isStar: Boolean = false,
    val folder: String = "",
    val itemType: ItemType = ItemType.OTHER
)
