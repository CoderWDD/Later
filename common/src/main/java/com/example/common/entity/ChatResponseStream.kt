package com.example.common.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatResponseWrapper(
    @Json(name = "data") val chatResponseStream: ChatResponseStream
)

//@Json(name = "data")
@JsonClass(generateAdapter = true)
data class ChatResponseStream(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice (
    val finish_reason: String? = null,
    val index: Int,
    val delta: Delta
)

@JsonClass(generateAdapter = true)
data class Delta (
    val content: String? = null,
    val role: String? = null
)
