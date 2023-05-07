package com.example.common

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.common.log.CallStackLogInterceptor
import com.example.common.log.LaterLog
import com.example.common.log.LaterLogcatInterceptor
import com.example.common.room.ConversationDBManager
import com.therouter.TheRouter

class MyApplication: Application() {
    companion object{
        lateinit var instance: MyApplication
    }

    override fun attachBaseContext(base: Context?) {
        // 设置为debug模式，可以看到具体日志
        TheRouter.isDebug = true
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLaterLog()
    }

    private fun initLaterLog(){
        LaterLog.addInterceptor(CallStackLogInterceptor())
        LaterLog.addInterceptor(LaterLogcatInterceptor())
    }
}