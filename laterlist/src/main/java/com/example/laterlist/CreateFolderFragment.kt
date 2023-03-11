package com.example.laterlist

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.inputmethodservice.InputMethodService
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.common.custom.BaseFragment
import com.example.laterlist.callback.MenuItemDialogClickCallBack
import com.example.laterlist.databinding.FragmentCreateFolderBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateFolderFragment : DialogFragment() {
    private lateinit var viewBinding: FragmentCreateFolderBinding
    private var keyboardVisible  = false
    private var customDialogCallback: MenuItemDialogClickCallBack? = null

    companion object{
        fun newInstance(customDialogCallback: MenuItemDialogClickCallBack): CreateFolderFragment {
            val fragment = CreateFolderFragment()
            fragment.customDialogCallback = customDialogCallback
            return fragment
        }
    }


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
        viewBinding.folderNameEdittext.requestFocus()
        // 自动弹出软键盘
        Handler(Looper.getMainLooper()).postDelayed({ (requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(viewBinding.folderNameEdittext, 0) }, 100)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment\
        viewBinding = FragmentCreateFolderBinding.inflate(inflater, container, false)

        viewBinding.root.setOnApplyWindowInsetsListener { _, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val height = insets.getInsets(WindowInsets.Type.ime()).bottom
                viewBinding.root.setPadding(0, 0, 0, height)
            }
            insets
        }

        return viewBinding.root
    }

    override fun onStop() {
        super.onStop()
        dialog?.window?.decorView?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardListener)
        viewBinding.folderNameEdittext.text = null
    }

    private fun initClickListener(){
        // 注册取消的事件监听
        viewBinding.createFolderButtonCancel.setOnClickListener {
            // 执行自定义的取消事件
            customDialogCallback?.onCancelClickListener()
            onDismiss()
        }

        // 注册确定的事件监听
        viewBinding.createFolderButtonConfirm.setOnClickListener {
            // 执行自定义的确定事件
            customDialogCallback?.onConfirmClickListener(viewBinding.folderNameEdittext.text.toString())
            onDismiss()
        }
    }


    private fun onDismiss(){
        // 清空内容，防止下次弹出显示
        viewBinding.folderNameEdittext.text = null
        // 取消弹出的对话框
        dismiss()
    }

}