package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.ChatItemBinding
import com.example.common.entity.MsgEntity
import com.example.common.recyclerview.RVProxy

class MsgItemProxy: RVProxy<MsgEntity, MsgItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MsgItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MsgItemViewHolder,
        data: MsgEntity,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.name
            contentText.text = data.content
        }
    }
}

class MsgItemViewHolder (itemBinding: ChatItemBinding): RecyclerView.ViewHolder(itemBinding.root){
    var titleText: TextView
    var contentText: TextView
    init {
        titleText = itemBinding.chatTitle
        contentText = itemBinding.chatContent
    }
}