package com.example.notification.repository.service

import com.example.common.entity.ChatRequest
import com.example.common.entity.MsgEntity
import com.example.common.room.entities.MessageEntity
import com.example.notification.entity.MsgResponse
import kotlinx.coroutines.flow.Flow

interface MsgService {
    suspend fun sendMsg(msg: MsgEntity): Flow<MsgResponse>

    suspend fun getMsgListByConversationId(conversationId: Long): Flow<List<MessageEntity>>

    suspend fun createConversation(conversationName: String)
}