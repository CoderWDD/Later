package com.example.common.utils

import androidx.fragment.app.Fragment
import com.therouter.TheRouter

object TheRouterUtil {

    fun<T: Fragment> getFragmentByPath(path: String): T? {
        return TheRouter.build(path).createFragment<T>()
    }
}