package com.example.common.entity

import java.util.UUID

data class LaterFolderEntity @JvmOverloads constructor(
    // the id of the folder, should be unique
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val cnt: Int = 0,
)
