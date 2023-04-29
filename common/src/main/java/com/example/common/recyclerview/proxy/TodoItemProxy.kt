package com.example.common.recyclerview.proxy

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.LayoutTodoItemBinding
import com.example.common.entity.TodoItem
import com.example.common.entity.TodoState
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import java.text.SimpleDateFormat
import java.util.Locale

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
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        holder.apply {
            todoTitle.text = data.title
            val time = "${dateFormat.format(data.startTime)} - ${dateFormat.format(data.endTime)}"
            todoTime.text = time
            if (data.state == TodoState.DONE) {
                todoTitle.paintFlags = todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                todoCheckBox.isChecked = true
            }else {
                todoTitle.paintFlags = todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                todoCheckBox.isChecked = false
            }
        }
    }
}

class TodoRecyclerViewViewHolder(viewBinding: LayoutTodoItemBinding): RecyclerView.ViewHolder(viewBinding.root){
    var todoTitle: TextView
    var todoTime: TextView
    var todoCheckBox: CheckBox
    init {
        todoTitle = viewBinding.todoTitle
        todoTime = viewBinding.todoTime
        todoCheckBox = viewBinding.todoCheckbox
    }
}