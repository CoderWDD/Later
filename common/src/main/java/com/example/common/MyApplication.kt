package com.example.common

import android.app.Application
import android.content.Context
import android.view.View
import androidx.room.Room
import com.example.common.datastore.SettingDataStore
import com.example.common.log.CallStackLogInterceptor
import com.example.common.log.LaterLog
import com.example.common.log.LaterLogcatInterceptor
import com.example.common.room.ConversationDBManager
import com.therouter.TheRouter

class MyApplication: Application() {
    var loadingView: View? = null
    companion object{
        lateinit var instance: MyApplication
        private lateinit var openAISettingData: SettingDataStore.SettingDataParameters

        fun setSettingData(settingData: SettingDataStore.SettingDataParameters){
            openAISettingData = settingData
        }

        fun getSettingData(): SettingDataStore.SettingDataParameters{
            return openAISettingData
        }
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