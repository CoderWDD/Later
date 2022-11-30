package com.example.common.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// 用户抽象代理 RecyclerView.ViewHolder 的部分功能，从而实现在 Adapter 中可以独立实现不同的  viewHolder
abstract class RVProxy<T, VH: RecyclerView.ViewHolder> {
    abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindViewHolder(holder: VH, data: T, index: Int, action: ((Any?) -> Unit)? = null)
    open fun onBindViewHolder(holder: VH, data: T, index: Int, action: ((Any?) -> Unit)? = null, payloads: MutableList<Any>){
        onBindViewHolder(holder, data, index, action)
    }
}