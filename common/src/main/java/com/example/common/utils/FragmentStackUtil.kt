package com.example.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.common.R
import java.util.*

object FragmentStackUtil {
    fun popBackStack(fragment: Fragment) {
        popBackStack(fragment = fragment, fragment.parentFragmentManager)
    }

    fun popBackStack(fragment: Fragment, fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction().remove(fragment).commit()
        fragmentManager.beginTransaction().show(fragmentManager.fragments.first()).commit()
    }

    fun popBackStack(fragment: Fragment, fragmentActivity: FragmentActivity) {
        popBackStack(fragment = fragment, fragmentActivity.supportFragmentManager)
    }

    private fun isFragmentStackEmpty(fragmentManager: FragmentManager): Boolean {
        return fragmentManager.backStackEntryCount == 0
    }


    fun addToMainFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        tag: String? = fragment.tag,
        addToStack: Boolean = true,
        stackName: String? = fragment.tag
    ) {
        // 隐藏所有fragment
        fragmentManager.fragments.forEach {
            if (it.isVisible) {
                fragmentManager.beginTransaction().hide(it).commit()
            }
        }
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (addToStack) {
            fragmentTransaction.addToBackStack(stackName)
        }
        fragmentTransaction.add(R.id.fragment_container, fragment, tag)
        fragmentTransaction.commit()
    }

    fun replaceMainFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        tag: String? = fragment.tag,
        addToStack: Boolean = true,
        stackName: String? = fragment.tag
    ) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
        if (addToStack) {
            fragmentTransaction.addToBackStack(stackName)
        }
        fragmentTransaction.commit()
    }
}