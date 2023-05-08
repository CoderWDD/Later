package com.example.calendar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.calendar.repository.TodoItemRepository
import com.example.common.entity.TodoItem

class TodoViewModel: ViewModel() {
    private val todoRepository: TodoItemRepository by lazy { TodoItemRepository(viewModelScope) }

    fun getTodoListByDate(date: String) = todoRepository.getTodoListByDate(date).asLiveData()

    fun updateTodoItem(date: String, todoItem: TodoItem) = todoRepository.updateTodoItem(date = date, todoItem = todoItem).asLiveData()

    fun createTodoItem(date: String, todoItem: TodoItem) = todoRepository.createTodoItem(date = date, todoItem = todoItem).asLiveData()

    fun deleteTodoItem(date: String, todoItem: TodoItem) = todoRepository.deleteTodoItem(date = date, todoItem = todoItem).asLiveData()
}