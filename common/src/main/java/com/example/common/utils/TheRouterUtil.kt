package com.example.common.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.therouter.TheRouter

object TheRouterUtil {

    fun<T: Fragment> getFragmentByPath(path: String): T? {
        return TheRouter.build(path).createFragment<T>()
    }

    fun<T: Fragment> navToFragmentAdd(path: String, fragmentManager: FragmentManager, tag: String? = null, addToStack: Boolean = true, stackName: String? = null){
        val fragmentByPath = getFragmentByPath<T>(path)
        val fragment = fragmentByPath as Fragment ?: return
        navToFragmentAdd<T>(fragment, fragmentManager, tag, addToStack, stackName)
    }

    fun<T: Fragment> navToFragmentAdd(fragment: Fragment, fragmentManager: FragmentManager, tag: String? = null, addToStack: Boolean = true, stackName: String? = null){
        FragmentStackUtil.addToMainFragment(
            fragmentManager = fragmentManager,
            fragment = fragment,
            tag = tag ?: fragment.tag,
            addToStack = addToStack,
            stackName = stackName ?: fragment.tag
        )
    }

    fun<T: Fragment> navToFragmentReplace(path: String, fragmentManager: FragmentManager, tag: String? = null, addToStack: Boolean = true, stackName: String? = null){
        val fragment = getFragmentByPath<T>(path) as Fragment
        FragmentStackUtil.replaceMainFragment(
            fragmentManager = fragmentManager,
            fragment = fragment,
            tag = tag ?: fragment.tag,
            addToStack = addToStack,
            stackName = stackName ?: fragment.tag
        )
    }

}