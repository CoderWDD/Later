package com.example.common.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.common.R

class TodoItemNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val todoId = inputData.getString("todoId") ?: return Result.failure()
        val todoTitle = inputData.getString("todoTitle") ?: return Result.failure()

        val channelId = "todo_reminder_channel"
        val notificationTitle = "即将到期的待办事项：$todoTitle"
        val notificationBody = "距离待办事项「$todoTitle」结束还有半小时，请尽快完成！"

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setAutoCancel(true)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 在 Android O（API 26）及更高版本上，需要创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "待办事项提醒",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 显示通知
        notificationManager.notify(todoId.hashCode(), notificationBuilder.build())

        return Result.success()
    }
}