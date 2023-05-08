package com.example.common.network.service

import com.example.common.entity.ChatRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface OpenAiChatService {
    @POST("chat/completions")
    suspend fun getChatResponseByRequest(@Body chatRequest: ChatRequest): Response<ResponseBody>
}