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
import java.io.PrintWriter
import java.io.StringWriter

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
        setupExceptionHandler()
        instance = this
        initLaterLog()
    }

    private fun initLaterLog(){
        LaterLog.addInterceptor(CallStackLogInterceptor())
        LaterLog.addInterceptor(LaterLogcatInterceptor())
    }


    private fun setupExceptionHandler() {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            // 获取堆栈跟踪信息
            val sw = StringWriter()
            exception.printStackTrace(PrintWriter(sw))
            val exceptionAsString = sw.toString()

            // 将异常信息发送到服务器
            sendErrorReportToServer(exceptionAsString)

            // 调用系统默认的异常处理器
            defaultExceptionHandler?.uncaughtException(thread, exception)
        }
    }

    private fun sendErrorReportToServer(errorReport: String) {
        // 在这里实现将错误报告发送到服务器的逻辑
    }
}