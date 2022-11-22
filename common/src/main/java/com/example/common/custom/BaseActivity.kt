package com.example.common.custom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.therouter.TheRouter

class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheRouter.inject(this)
    }
}