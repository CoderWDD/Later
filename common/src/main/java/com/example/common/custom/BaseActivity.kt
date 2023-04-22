package com.example.common.custom

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import com.example.common.extents.hideSystemStatusBar
import com.example.common.utils.FragmentStackUtil
import com.therouter.TheRouter

abstract class BaseActivity<VB: ViewBinding>(private val bindingInflate: (inflate: LayoutInflater) -> VB): AppCompatActivity() {
    private var _viewBinding: VB? = null

    val viewBinding: VB
        get() = _viewBinding ?: throw java.lang.IllegalArgumentException("Binding cannot be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheRouter.inject(this)
        _viewBinding = bindingInflate.invoke(layoutInflater)
        setContentView(viewBinding.root)
        onCreate()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    abstract fun onCreate()
}