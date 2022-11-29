package com.example.common.log

import android.util.Log

class LaterLogcatInterceptor : LaterLogInterceptor {
    override fun log(priority: Int, tag: String, logMes: String, chain: LaterLogChain) {
        if (enable()){
            Log.println(priority, tag, logMes)
        }
        chain.proceed(priority, tag, logMes)
    }

    override fun enable(): Boolean {
        return true
    }
}