package com.example.common.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.common.R
import java.util.*

object FragmentStackUtil {
    private val currentFragment: Fragment? = null
    private val fragmentStack = Stack<Fragment>()

    fun popBackStack(fragment: Fragment) {
        popBackStack(fragment.parentFragmentManager)
    }

    fun popBackStack(fragmentManager: FragmentManager) {
        if (isFragmentStackEmpty(fragmentManager)) return
        fragmentManager.popBackStack()
    }

    fun popBackStack(fragmentActivity: FragmentActivity) {
        popBackStack(fragmentActivity.supportFragmentManager)
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
        val fragmentTransaction = fragmentManager.beginTransaction()
        // 如果当前有fragment，就先将其隐藏
        if (!fragmentStack.empty()) {
            fragmentTransaction.hide(fragmentStack.peek())
        }
        fragmentTransaction.add(R.id.fragment_container, fragment, tag)
        if (addToStack) {
            fragmentTransaction.addToBackStack(stackName)
        }
        fragmentStack.push(fragment)
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
        fragmentStack.pop()
        fragmentStack.push(fragment)
        if (addToStack) {
            fragmentTransaction.addToBackStack(stackName)
        }
        fragmentTransaction.commit()
    }

    fun navBack() {
        if (fragmentStack.isNotEmpty()) fragmentStack.pop()
    }
}