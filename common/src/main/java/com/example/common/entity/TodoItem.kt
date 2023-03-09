package com.example.common.entity

data class TodoItem(
    val id: Int,
    val task: String,
    val isCompleted: Boolean,
    val isDeleted: Boolean,
    val createTime: String,
    val updateTime: String,
    val completedTime: String,
    val deletedTime: String,
    val todoTime: String,
    val isRepeat: Boolean,
)
