package com.example.common.unify

sealed class ResBody<out T> {
    object Cancel : ResBody<Nothing>()
    object Loading : ResBody<Nothing>()
    object Success : ResBody<Nothing>()
    data class Error<out T>(val code: Int, val msg: String, val other: T) : ResBody<T>()
}