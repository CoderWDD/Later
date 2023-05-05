package com.example.common.network.service

import android.database.Observable
import com.example.common.entity.ChatRequest
import com.example.common.entity.ChatResponseStream
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface OpenAiChatService {
    @POST("/chat/completions")
    suspend fun getChatResponseByRequest(@Body chatRequest: ChatRequest): retrofit2.Response<ResponseBody>
}