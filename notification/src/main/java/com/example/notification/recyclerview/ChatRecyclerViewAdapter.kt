package com.example.notification.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.common.room.entities.MessageEntity
import com.example.notification.R


class ChatRecyclerViewAdapter: RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>() {
    private val msgList = mutableListOf<MessageEntity>()
    // 静态内部类
    inner class ChatViewHolder(itemView: View, private val contentWidth: Int): RecyclerView.ViewHolder(itemView) {
        val msgContent: TextView
        val msgTime: TextView
        val msgAvatar: ImageView

        init {
            msgContent = itemView.findViewById(R.id.chat_message_body)
            msgTime = itemView.findViewById(R.id.chat_message_time)
            msgAvatar = itemView.findViewById(R.id.chat_message_avatar)
            msgContent.maxWidth = contentWidth
        }
    }

    enum class MsgType{
        SEND,
        RECEIVE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View = when (viewType) {
            MsgType.SEND.ordinal -> LayoutInflater.from(parent.context).inflate(R.layout.chat_message_item_sender, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.chat_message_item_receiver, parent, false)
        }
        val screenWidth = parent.resources.displayMetrics.widthPixels
        return ChatViewHolder(view, (screenWidth * 0.6667).toInt())
    }

    override fun getItemCount(): Int = msgList.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.apply {
            msgContent.text = msgList[position].messageContent
            msgTime.text = msgList[position].messageTime
            msgAvatar.setImageResource(R.drawable.baseline_account_circle_24)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (msgList[position].messageIsSender) {
            MsgType.SEND.ordinal
        } else {
            MsgType.RECEIVE.ordinal
        }
    }

    fun addMsg(msg: MessageEntity){
        msgList.add(msg)
        notifyItemInserted(msgList.size - 1)
    }

    fun addMsgList(msgList: List<MessageEntity>){
        this.msgList.addAll(msgList)
        notifyItemRangeInserted(this.msgList.size - msgList.size, msgList.size)
    }
}