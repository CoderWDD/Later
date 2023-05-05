package com.example.common.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.common.entity.MsgEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "message")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "message_id") val messageId: Long = 0,
    @ColumnInfo(name = "message_content") val messageContent: String,
    @ColumnInfo(name = "message_type") val messageType: MessageType = MessageType.TEXT,
    @ColumnInfo(name = "message_time") val messageTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    @ColumnInfo(name = "message_conversationId") val messageConversationId: Long,
    @ColumnInfo(name = "message_sender") val messageSender: String,
){
    companion object{
        fun fromMsgEntity(msgEntity: MsgEntity): MessageEntity{
            return MessageEntity(
                messageContent = msgEntity.content,
                messageConversationId = msgEntity.conversationId,
                messageSender = msgEntity.name
            )
        }
    }

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