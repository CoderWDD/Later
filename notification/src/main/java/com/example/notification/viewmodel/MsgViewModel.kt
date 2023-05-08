package com.example.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.room.entities.ConversationEntity
import com.example.common.room.entities.MessageEntity
import com.example.notification.entity.MsgResponse
import com.example.notification.repository.MsgRepository
import kotlinx.coroutines.flow.Flow

class MsgViewModel: ViewModel() {
    var conversationId: Long = 0
        set(value) {
            if (value < 0) throw IllegalArgumentException("conversationId must be greater than 0")
            field = value
        }

    private val msgRepository: MsgRepository = MsgRepository()

    suspend fun sendMsg(msg: MessageEntity): Flow<MsgResponse> = msgRepository.sendMsg(msg = msg)

    suspend fun createConversation(conversationName: String): Long = msgRepository.createConversation(conversationName = conversationName)

    suspend fun getMsgListByConversationId(conversationId: Long): List<MessageEntity> = msgRepository.getMsgListByConversationId(conversationId = conversationId)

    suspend fun deleteConversation(conversation: ConversationEntity) = msgRepository.deleteConversation(conversation = conversation)

    suspend fun updateConversation(conversation: ConversationEntity) = msgRepository.updateConversation(conversation = conversation)

    suspend fun getConversationList(): List<ConversationEntity> = msgRepository.getConversationList()

    suspend fun getConversationById(conversationId: Long): ConversationEntity = msgRepository.getConversationById(conversationId)
}