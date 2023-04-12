package com.example.common.reporesource


sealed class Resource<out T> {
    data class Success<out T>(val data: T, val status: Status) : Resource<T>()
    data class Error(val status: Status, val message: String? = null) : Resource<Nothing>()
    data class Loading<out T>(val data: T? = null, val status: Status) : Resource<T>()
    data class Cached<out T>(val data: T? = null, val status: Status) : Resource<T>()
    object ReAuthenticate : Resource<Nothing>()
    object Logout : Resource<Nothing>()

    companion object {
        fun <T> success(data: T, status: Status = Status.SUCCESS): Resource<T> = Success(data = data, status = status)
        fun error(message: String?, status: Status = Status.ERROR): Resource<Nothing> = Error(status = status, message = message)
        fun <T> loading(data: T? = null, status: Status = Status.LOADING): Resource<T> = Loading(data = data, status = status)
        fun <T> cached(data: T? = null, status: Status = Status.CACHED): Resource<T> = Cached(data = data, status = status)
        fun reAuthenticate(): Resource<Nothing> = ReAuthenticate
        fun logout(): Resource<Nothing> = Logout
    }

    enum class Status {
        SUCCESS, ERROR, LOADING, CACHED, REAUTH, LOGOUT
    }
}
