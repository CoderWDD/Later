package com.example.later

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.common.constants.RoutePathConstant
import com.example.common.utils.FragmentStackUtil
import com.example.home.HomeFragment
import com.therouter.TheRouter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val createFragment = TheRouter.build(RoutePathConstant.HomeFragment).createFragment<HomeFragment>()
//        FragmentStackUtil.addToMainFragment(supportFragmentManager, createFragment!!, addToStack = true, tag = null, stackName = "")
    }
}