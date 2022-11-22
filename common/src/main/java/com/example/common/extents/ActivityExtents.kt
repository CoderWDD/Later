package com.example.common.extents

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets

fun Activity.hideSystemStatusBar(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        this.window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
    }else{
        this.window.decorView.systemUiVisibility = this.window.decorView.visibility or View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}