package com.example.common.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "message_id") val messageId: Long,
    @ColumnInfo(name = "message_content") val messageContent: String,
    @ColumnInfo(name = "message_type") val messageType: String,
    @ColumnInfo(name = "message_time") val messageTime: String,
    @ColumnInfo(name = "message_conversationId") val messageConversationId: Int,
    @ColumnInfo(name = "message_sender") val messageSender: Int,
)