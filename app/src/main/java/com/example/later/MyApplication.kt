package com.example.later

import android.app.Application
import android.content.Context
import com.therouter.TheRouter

class MyApplication: Application() {
    override fun attachBaseContext(base: Context?) {
        // 设置为debug模式，可以看到具体日志
        TheRouter.isDebug = true
        super.attachBaseContext(base)
    }
}