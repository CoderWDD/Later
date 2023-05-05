package com.example.common.room.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ConversationWithMessageList(
    @Embedded val conversation: ConversationEntity,
    @Relation(
        parentColumn = "conversation_id",
        entityColumn = "message_conversationId"
    )
    val messageList: List<MessageEntity>
)