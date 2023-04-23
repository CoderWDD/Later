package com.example.common.entity

import java.util.UUID

data class TodoItem(
    val id: String = UUID.randomUUID().toString(),
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val state: TodoState = TodoState.TODO,
    val title: String = "",
    val description: String?,
    val isFromLaterViewItem: Boolean = false,
    val laterViewItem: LaterViewItem? = null
)

enum class TodoState {
    TODO, DONE
}