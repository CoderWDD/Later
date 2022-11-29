package com.example.common.log

class LaterLogChain (
    private val interceptors: List<LaterLogInterceptor>,
    private val index: Int = 0
){
    fun proceed(priority: Int, tag: String, logMes: String){
        // 获取下一个链的 chain
        val nextChain = LaterLogChain(interceptors, index + 1)
        // 获取当前的 interceptor
        val interceptor = interceptors.getOrNull(index)
        // 调用当前 interceptor 的 log 方法
        interceptor?.log(priority = priority, tag = tag, logMes = logMes, chain = nextChain)
    }
}