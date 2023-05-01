package com.example.notification.repository.service

import com.example.common.entity.MsgEntity

interface MsgService {
    fun sendMsg(msg: MsgEntity)
}