package com.example.common.extents

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.common.MyApplication
import com.example.common.R
import com.example.common.entity.TodoItem
import com.example.common.workmanager.TodoItemNotificationWorker
import java.util.concurrent.TimeUnit

fun Activity.hideSystemStatusBar(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        this.window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
    }else{
        this.window.decorView.systemUiVisibility = this.window.decorView.visibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

fun Activity.showToast(content: String){
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
}

fun Activity.showLoadingView() {
    val app = applicationContext as MyApplication
    if (app.loadingView == null) {
        app.loadingView = layoutInflater.inflate(R.layout.loading_view, findViewById<ViewGroup>(android.R.id.content), false)
    }
    app.loadingView?.let {
        (findViewById<ViewGroup>(android.R.id.content)).addView(it)
    }
}

fun Activity.hideLoadingView() {
    val app = applicationContext as MyApplication
    app.loadingView?.let {
        (findViewById<ViewGroup>(android.R.id.content)).removeView(it)
    }
}

fun Activity.scheduleNotification(todoItem: TodoItem) {
    val reminderTime = todoItem.endTime - (30 * 60 * 1000) // 提前半小时

    if (reminderTime <= System.currentTimeMillis()) {
        return
    }

    val delay = reminderTime - System.currentTimeMillis()

    val inputData = Data.Builder()
        .putString("todoId", todoItem.id)
        .putString("todoTitle", todoItem.title)
        .build()

    val workRequest: WorkRequest = OneTimeWorkRequestBuilder<TodoItemNotificationWorker>()
        .setInputData(inputData)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(this).enqueue(workRequest)
}