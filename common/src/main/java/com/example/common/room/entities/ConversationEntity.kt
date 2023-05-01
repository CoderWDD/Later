package com.example.common.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversation")
data class ConversationEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo("conversation_id") val conversationId: Long,
    @ColumnInfo(name = "conversation_name") val conversationName: String?,
    @ColumnInfo(name = "conversation_avatar") val conversationAvatar: String?,
    @ColumnInfo(name = "conversation_last_message_time") val conversationLastMessageTime: String?,
    @ColumnInfo(name = "conversation_create_time") val conversationLastMessage: String?,
)