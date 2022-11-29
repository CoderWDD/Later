package com.example.common.log

interface LaterLogInterceptor {
    fun log(priority: Int, tag: String, logMes: String, chain: LaterLogChain)
    fun enable(): Boolean
}