package com.example.common.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitAuthInterceptor: Interceptor {

    private fun getOpenAiToken(): String {
        return "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"
    }

    private fun getOpenAiOrganization(): String {
        return ""
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${getOpenAiToken()}")
//            .addHeader("OpenAI-Organization", getOpenAiOrganization())
            .build()

        return chain.proceed(newRequest)
    }
}