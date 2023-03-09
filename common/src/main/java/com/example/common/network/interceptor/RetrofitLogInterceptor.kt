package com.example.common.network.interceptor

import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import okhttp3.*
import okio.Buffer
import java.util.*

class RetrofitLogInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("retrofit", "start: ===============================", )

        val startTime = SystemClock.currentThreadTimeMillis()
        val request = chain.request()
        val headers = request.headers

        Log.e("retrofit", "method: ${request.method}" )
        Log.e("retrofit", "url: ${request.url}" )
        Log.e("retrofit", "isHttps: ${request.isHttps}" )
        Log.e("retrofit", "headers: $headers" )
        Log.e("retrofit", "headers size: ${headers.size}" )

        headers.forEach { Log.e("retrofit", "${it.first}  : ${it.second}") }

        val requestBody = request.body
        val readRequestParamString = readRequestParamString(requestBody)

        Log.e("retrofit", "request Body: $readRequestParamString", )

        // 放行请求
        val response = chain.proceed(request)
        val responseBody = response.body

        Log.e("retrofit", "respond code: ${response.code}")

        var responseString = ""
        if (responseBody != null){
            responseString = if (isPlainText(responseBody.contentType())) {
                readContent(response)
            } else {
                "other-type=" + responseBody.contentType()
            }
        }

        Log.e("retrofit", "respond body: $responseString", )

        val endTime = SystemClock.currentThreadTimeMillis()
        Log.e("retrofit", "time : ${endTime - startTime}")
        Log.e("retrofit", "start: ===============================")
        return response
    }


    private fun readRequestParamString(requestBody: RequestBody?): String {
        val paramString: String
        if (requestBody is MultipartBody) { //判断是否有文件
            val sb = StringBuilder()
            val parts: List<MultipartBody.Part> = requestBody.parts
            var partBody: RequestBody
            var i = 0
            val size = parts.size
            while (i < size) {
                partBody = parts[i].body
                i++
                if (sb.isNotEmpty()) {
                    sb.append(",")
                }
                if (isPlainText(partBody.contentType())) {
                    sb.append(readContent(partBody))
                } else {
                    sb.append("other-param-type=").append(partBody.contentType())
                }
            }
            paramString = sb.toString()
        } else {
            paramString = readContent(requestBody)
        }
        return paramString
    }

    private fun readContent(response: Response?): String {
        if (response == null) {
            return ""
        }
        try {
            return response.peekBody(Long.MAX_VALUE).string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun readContent(body: RequestBody?): String {
        if (body == null) {
            return ""
        }
        val buffer = Buffer()
        try {
            //小于2m
            if (body.contentLength() <= 2 * 1024 * 1024) {
                body.writeTo(buffer)
            } else {
                return "content is more than 2M"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return buffer.readUtf8()
    }

    private fun isPlainText(mediaType: MediaType?): Boolean {
        if (null != mediaType) {
            var mediaTypeString = mediaType.toString()
            if (!TextUtils.isEmpty(mediaTypeString)) {
                mediaTypeString = mediaTypeString.lowercase(Locale.getDefault())
                if (mediaTypeString.contains("text") || mediaTypeString.contains("application/json")) {
                    return true
                }
            }
        }
        return false
    }
}