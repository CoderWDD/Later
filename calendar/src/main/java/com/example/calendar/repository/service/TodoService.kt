package com.example.calendar.repository.service

import com.example.common.entity.TodoItem
import com.example.common.reporesource.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface TodoService {
    // get to do list by date
    fun getTodoListByDate(date: String): MutableSharedFlow<Resource<List<TodoItem>>>

    // create to to item
    fun createTodoItem(date: String, todoItem: TodoItem): Flow<Resource<String>>

    // update to do item
    fun updateTodoItem(date: String, todoItem: TodoItem): Flow<Resource<String>>

    // delete to do item
    fun deleteTodoItem(date: String, todoItem: TodoItem): Flow<Resource<String>>
}