package com.example.laterlist

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.common.custom.BaseDialogFragment
import com.example.common.entity.LaterViewItem
import com.example.common.callback.MenuItemDialogClickCallBack
import com.example.laterlist.databinding.FragmentCreateTagBinding

class CreateTagFragment : BaseDialogFragment<FragmentCreateTagBinding>(FragmentCreateTagBinding::inflate) {
    private var customDialogCallback: MenuItemDialogClickCallBack<String>? = null

    companion object{
        fun newInstance(customDialogCallback: MenuItemDialogClickCallBack<String>): CreateTagFragment {
            val fragment = CreateTagFragment()
            fragment.customDialogCallback = customDialogCallback
            return fragment
        }
    }

    override fun onCreateView() {}

    override fun initClickListener() {
        // 注册取消的事件监听
        viewBinding.createTagButtonCancel.setOnClickListener {
            // 执行自定义的取消事件
            customDialogCallback?.onCancelClickListener()
            onDismiss()
        }

        // 注册确定的事件监听
        viewBinding.createTagButtonConfirm.setOnClickListener {
            // 执行自定义的确定事件
            customDialogCallback?.onConfirmClickListener(viewBinding.tagNameEdittext.text.toString())
            onDismiss()
        }
    }

    override fun focusWidget(): EditText? = viewBinding.tagNameEdittext

    private fun onDismiss(){
        // 清空内容，防止下次弹出显示
        viewBinding.tagNameEdittext.text = null
        // 取消弹出的对话框
        dismiss()
    }

}