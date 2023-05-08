package com.example.common.entity

data class ChatRequest(
    val model: String,
    val messages: MutableList<MessageItem> = mutableListOf(),
    val temperature: Double = 1.0,
    val top_p: Double = 1.0,
    val n: Int = 1,
    val stream: Boolean = true,
    // Up to 4 sequences where the API will stop generating further tokens.
    val stop: List<String>? = null,
//    val max_tokens: Int = Int.MAX_VALUE,
    // Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
    val presence_penalty: Double = 0.0,
    // Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
    val frequency_penalty: Double = 0.0,
)
