package com.example.common.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "message")
data class MessageEntity @JvmOverloads constructor (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "message_id") val messageId: Long = 0,
    @ColumnInfo(name = "message_content") val messageContent: String,
    @ColumnInfo(name = "message_type") val messageType: MessageType = MessageType.TEXT,
    @ColumnInfo(name = "message_time") val messageTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    @ColumnInfo(name = "message_conversationId") val messageConversationId: Long,
    @ColumnInfo(name = "message_sender") var messageSender: String,
    @ColumnInfo(name = "message_is_sender") val messageIsSender: Boolean = true,
){
    enum class MessageType(type: String){
        TEXT("text"),
        IMAGE("image"),
        VIDEO("video"),
        AUDIO("audio"),
        FILE("file"),
        LOCATION("location"),
        LINK("link"),
        CONTACT("contact"),
        STICKER("sticker"),
        GIFT("gift"),
        SYSTEM("system"),
        NOTIFICATION("notification"),
        OTHER("other")
    }
}