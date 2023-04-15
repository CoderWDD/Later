package com.example.laterlist

import android.widget.EditText
import com.example.common.custom.BaseDialogFragment
import com.example.laterlist.callback.MenuItemDialogClickCallBack
import com.example.laterlist.databinding.FragmentCreateFolderBinding

class CreateFolderFragment : BaseDialogFragment<FragmentCreateFolderBinding>(FragmentCreateFolderBinding::inflate) {
    private var customDialogCallback: MenuItemDialogClickCallBack<String>? = null

    companion object{
        fun newInstance(customDialogCallback: MenuItemDialogClickCallBack<String>): CreateFolderFragment {
            val fragment = CreateFolderFragment()
            fragment.customDialogCallback = customDialogCallback
            return fragment
        }
    }

    override fun onCreateView() {}

    override fun initClickListener(){
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

    override fun focusWidget(): EditText = viewBinding.folderNameEdittext


    private fun onDismiss(){
        // 清空内容，防止下次弹出显示
        viewBinding.folderNameEdittext.text = null
        // 取消弹出的对话框
        dismiss()
    }
}