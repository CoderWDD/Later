package com.example.common.network.interceptor

import com.example.common.MyApplication
import okhttp3.Interceptor
import okhttp3.Response

class RetrofitAuthInterceptor: Interceptor {

    private fun getOpenAiToken(): String {
        return MyApplication.getSettingData().token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${getOpenAiToken()}")
            .build()

        return chain.proceed(newRequest)
    }
}