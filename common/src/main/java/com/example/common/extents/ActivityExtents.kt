package com.example.common.extents

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Toast
import com.example.common.MyApplication
import com.example.common.R

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