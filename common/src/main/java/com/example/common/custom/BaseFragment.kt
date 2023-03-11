package com.example.common.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.therouter.TheRouter

/**
 * 偷懒的写法，可以避免在每个fragment中都写一遍inflate
 */
abstract class BaseFragment<VB: ViewBinding>(private val bindingInflater: (inflate: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> VB): Fragment() {
    private var _viewBinding: VB? = null

    val viewBinding: VB
        get() = _viewBinding ?: throw java.lang.IllegalArgumentException("Binding cannot be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = bindingInflater.invoke(inflater, container, false)
        onCreateView()
        return viewBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        TheRouter.inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    abstract fun onCreateView()
}