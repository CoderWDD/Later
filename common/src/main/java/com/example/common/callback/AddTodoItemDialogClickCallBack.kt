package com.example.common.callback

import com.example.common.entity.TodoItem

interface AddTodoItemDialogClickCallBack<T> {
    fun onConfirmClickListener(todoItem: T, date: String)
}