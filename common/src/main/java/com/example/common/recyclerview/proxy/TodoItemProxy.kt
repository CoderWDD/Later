package com.example.common.recyclerview.proxy

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.LayoutTodoItemBinding
import com.example.common.entity.TodoItem
import com.example.common.entity.TodoState
import com.example.common.recyclerview.RVProxy

class TodoItemProxy: RVProxy<TodoItem, TodoRecyclerViewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val todoItemBinding =
            LayoutTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoRecyclerViewViewHolder(todoItemBinding)
    }

    override fun onBindViewHolder(
        holder: TodoRecyclerViewViewHolder,
        data: TodoItem,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            todoTitle.text = data.title
            todoTime.text = data.startTime.toString()
            if (data.state == TodoState.DONE) todoTitle.paintFlags = todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}

class TodoRecyclerViewViewHolder(viewBinding: LayoutTodoItemBinding): RecyclerView.ViewHolder(viewBinding.root){
    var todoTitle: TextView
    var todoTime: TextView
    init {
        todoTitle = viewBinding.todoTitle
        todoTime = viewBinding.todoTime
    }
}