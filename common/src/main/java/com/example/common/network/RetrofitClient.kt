package com.example.common.network

import com.example.common.entity.ChatResponseStream
import com.example.common.network.interceptor.RetrofitAuthInterceptor
import com.example.common.network.interceptor.RetrofitLogInterceptor
import com.example.common.network.service.OpenAiChatService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.openai.com/v1/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(RetrofitAuthInterceptor())
            .addInterceptor(RetrofitLogInterceptor())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val moshi: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    val chatServiceClient: OpenAiChatService by lazy { retrofit.create(OpenAiChatService::class.java) }

}