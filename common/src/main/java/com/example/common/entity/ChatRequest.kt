package com.example.common.entity

data class ChatRequest(
    val model: String,
    val message: List<MsgEntity>,
    val temperature: Double = 1.0,
    val top_p: Double = 1.0,
    val n: Int = 1,
    val stream: Boolean = true,
    // Up to 4 sequences where the API will stop generating further tokens.
    val stop: Array<String>? = null,
    val max_tokens: Int = Int.MAX_VALUE,
    // Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
    val presence_penalty: Double = 0.0,
    // Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
    val frequency_penalty: Double = 0.0,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatRequest

        if (model != other.model) return false
        if (message != other.message) return false
        if (temperature != other.temperature) return false
        if (top_p != other.top_p) return false
        if (n != other.n) return false
        if (stream != other.stream) return false
        if (stop != null) {
            if (other.stop == null) return false
            if (!stop.contentEquals(other.stop)) return false
        } else if (other.stop != null) return false
        if (max_tokens != other.max_tokens) return false
        if (presence_penalty != other.presence_penalty) return false
        if (frequency_penalty != other.frequency_penalty) return false

        return true
    }

    override fun hashCode(): Int {
        var result = model.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + temperature.hashCode()
        result = 31 * result + top_p.hashCode()
        result = 31 * result + n
        result = 31 * result + stream.hashCode()
        result = 31 * result + (stop?.contentHashCode() ?: 0)
        result = 31 * result + max_tokens
        result = 31 * result + presence_penalty.hashCode()
        result = 31 * result + frequency_penalty.hashCode()
        return result
    }
}
