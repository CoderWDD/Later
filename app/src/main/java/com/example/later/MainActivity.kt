package com.example.later

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseActivity
import com.example.common.extents.hideSystemStatusBar
import com.example.common.log.LaterLog
import com.example.common.utils.FragmentStackUtil
import com.example.common.utils.TheRouterUtil
import com.example.home.HomeFragment
import com.example.later.databinding.ActivityMainBinding
import com.therouter.TheRouter

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreate() {
        TheRouterUtil.navToFragmentAdd<HomeFragment>(RoutePathConstant.HomeFragment, supportFragmentManager)
    }
}