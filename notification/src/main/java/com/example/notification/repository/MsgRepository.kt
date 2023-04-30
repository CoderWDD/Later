package com.example.notification.repository

import com.example.notification.repository.service.MsgService
import kotlinx.coroutines.CoroutineScope

class MsgRepository(private val viewModelScope: CoroutineScope): MsgService {

}