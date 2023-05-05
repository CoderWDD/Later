package com.example.common.entity

data class ChatResponseStream(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice (
    val finish_reason: String? = null,
    val index: Int,
    val delta: Delta
)

data class Delta (
    val content: String? = null,
    val role: String? = null
)
