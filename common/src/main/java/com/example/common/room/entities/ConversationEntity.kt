package com.example.common.room.entities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.logging.SimpleFormatter

@Entity(tableName = "conversation")
data class ConversationEntity @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("conversation_id") val conversationId: Long = 0,
    @ColumnInfo(name = "conversation_name") val conversationName: String,
    @ColumnInfo(name = "conversation_avatar") val conversationAvatar: String? = null,
    @ColumnInfo(name = "conversation_last_message_time") val conversationLastMessageTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    @ColumnInfo(name = "conversation_create_time") val conversationLastMessage: String? = null,
)