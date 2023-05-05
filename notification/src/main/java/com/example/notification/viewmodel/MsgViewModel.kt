package com.example.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.entity.MsgEntity
import com.example.notification.entity.MsgResponse
import com.example.notification.repository.MsgRepository
import kotlinx.coroutines.flow.Flow

class MsgViewModel: ViewModel() {
    private val msgRepository: MsgRepository = MsgRepository(viewModelScope)

    suspend fun sendMsg(msg: MsgEntity): Flow<MsgResponse> = msgRepository.sendMsg(msg = msg)

    suspend fun createConversation(conversationName: String) = msgRepository.createConversation(conversationName = conversationName)
}