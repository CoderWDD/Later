package com.example.common.recyclerview.proxy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.databinding.ChatItemBinding
import com.example.common.recyclerview.RVProxy
import com.example.common.room.entities.ConversationEntity

class ConversationItemProxy: RVProxy<ConversationEntity, MsgItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MsgItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MsgItemViewHolder,
        data: ConversationEntity,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        holder.apply {
            titleText.text = data.conversationName
            contentText.text = data.conversationLastMessageTime
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