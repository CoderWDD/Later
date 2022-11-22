package com.example.common.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.therouter.TheRouter

object TheRouterUtil {
    fun <T: Fragment?> navigateToFragment(context: Context, url: String, fragment: T){
        TheRouter.build(url = url)
            .navigation(context = context)
    }
}