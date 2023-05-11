package com.example.common.entity

import java.util.UUID

data class LaterTagEntity @JvmOverloads constructor(
    var key: String = "",
    var cnt: Int = 0,
    val id: String = UUID.randomUUID().toString(),
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val name: String = "default tag",
    val laterViewItem: MutableMap<String, TagListItem> = mutableMapOf()
)