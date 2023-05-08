package com.example.common.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.common.R
import java.util.*

object FragmentStackUtil {
    private var containerViewId: Int = 0
        get() {
            if (field == 0) {
                return R.id.fragment_container
            }
            return field
        }

    private lateinit var fragmentManager: FragmentManager

    fun init(@IdRes containerViewId: Int, fragmentManager: FragmentManager){
        this.containerViewId = containerViewId
        this.fragmentManager = fragmentManager

    }

    fun addFragment(fragment: Fragment) {
        // 使用Handler来将新的事务加入到消息队列中，从而确保在执行新的事务时，FragmentManager已经完成了当前事务的执行
        Handler(Looper.getMainLooper()).post {
            fragmentManager.beginTransaction().apply {
                val currentFragment = currentFragment
                currentFragment?.let { hide(it) }
                add(containerViewId, fragment, fragment.javaClass.simpleName)
                addToBackStack(fragment.javaClass.simpleName)
                commit()
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        Handler(Looper.getMainLooper()).post {
            fragmentManager.beginTransaction().apply {
                val currentFragment = currentFragment
                currentFragment?.let { hide(it) }
                replace(containerViewId, fragment, fragment.javaClass.simpleName)
                addToBackStack(fragment.javaClass.simpleName)
                commit()
            }
        }
    }

    fun showFragment(fragment: Fragment) {
        Handler(Looper.getMainLooper()).post {
            fragmentManager.beginTransaction().apply {
                val currentFragment = currentFragment
                currentFragment?.let { hide(it) }
                show(fragment)
                commit()
            }
        }
    }

    val currentFragment: Fragment?
        get() = fragmentManager.findFragmentById(containerViewId)

    fun removeFragment(fragment: Fragment) {
        Handler(Looper.getMainLooper()).post {
            fragmentManager.beginTransaction().apply {
                remove(fragment)
                commit()
            }
        }
    }

    fun goBack() {
        fragmentManager.popBackStack()
    }

    fun isFragmentVisible(fragment: Fragment): Boolean {
        return fragment.isVisible
    }
}