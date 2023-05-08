package com.example.notification.repository.service

import com.example.common.room.entities.ConversationEntity
import com.example.common.room.entities.MessageEntity
import com.example.notification.entity.MsgResponse
import kotlinx.coroutines.flow.Flow

interface MsgService {
    suspend fun sendMsg(msg: MessageEntity): Flow<MsgResponse>

    suspend fun getMsgListByConversationId(conversationId: Long): List<MessageEntity>

    suspend fun createConversation(conversationName: String): Long

    suspend fun deleteConversation(conversation: ConversationEntity)

    suspend fun updateConversation(conversation: ConversationEntity)

    suspend fun getConversationList(): List<ConversationEntity>

    suspend fun getConversationById(conversationId: Long): ConversationEntity

}