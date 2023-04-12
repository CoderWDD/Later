package com.example.common.unify

sealed class ResBody<out T> {
    object Cancel : ResBody<Nothing>()
    object Loading : ResBody<Nothing>()
    data class Error<out T>(val code: Int, val msg: String) : ResBody<T>()
    data class Success<T>(
        val code: Int,
        val msg: String,
        val data: T? = null
    ) : ResBody<T>()
}