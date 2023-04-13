package com.example.common.custom

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<VB: ViewBinding>(private val bindingInflater: (inflate: LayoutInflater, root: ViewGroup?, attachToRoot: Boolean) -> VB): DialogFragment() {
    private var _viewBinding: VB? = null
    private var keyboardVisible  = false
    private var focusEditText: EditText? = null

    val viewBinding: VB
        get() = _viewBinding ?: throw java.lang.IllegalArgumentException("Binding cannot be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = bindingInflater.invoke(inflater, container, false)

        viewBinding.root.setOnApplyWindowInsetsListener { _, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val height = insets.getInsets(WindowInsets.Type.ime()).bottom
                viewBinding.root.setPadding(0, 0, 0, height)
            }
            insets
        }

        onCreateView()
        return viewBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        // 注册事件监听
        initClickListener()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 如果是 Android 11 及以上版本，使用新的 API
            dialog?.window?.setDecorFitsSystemWindows(false)
        }else {
            // 如果是 Android 11 以下版本，使用旧的 API
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        // 添加软键盘监听器
        dialog?.window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardListener)

        // 取消默认显示在屏幕中间的行为，改为显示在屏幕底部
        val window = dialog!!.window
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params

        // 先获取焦点，因为软键盘弹出需要有以及获取到焦点的控件
        focusEditText = focusWidget() ?: return
        focusEditText!!.requestFocus()
        // 自动弹出软键盘
        Handler(Looper.getMainLooper()).postDelayed({ (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(focusEditText, 0) }, 100)
    }

    override fun onStop() {
        super.onStop()
        dialog?.window?.decorView?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardListener)
        focusEditText?.text = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    abstract fun initClickListener()

    abstract fun onCreateView()

    abstract fun focusWidget(): EditText?

    // 软键盘监听器
    private val keyboardListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        dialog?.window?.decorView?.getWindowVisibleDisplayFrame(rect)
        val screenHeight = dialog?.window?.decorView?.rootView?.height ?: 0
        val keypadHeight = screenHeight - rect.bottom
        if (keypadHeight > screenHeight * 0.15) {
            // keyboard is visible
            if (!keyboardVisible) {
                dialog?.window?.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
                keyboardVisible = true
                // do something
            }
        } else {
            // keyboard is hidden
            if (keyboardVisible) {
                dialog?.window?.setGravity(Gravity.CENTER)
                keyboardVisible = false
                // do something
            }
        }
    }
}