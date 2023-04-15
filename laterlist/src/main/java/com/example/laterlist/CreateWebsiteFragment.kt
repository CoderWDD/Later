package com.example.laterlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.common.custom.BaseDialogFragment
import com.example.common.entity.ItemType
import com.example.common.entity.LaterViewItem
import com.example.laterlist.callback.MenuItemDialogClickCallBack
import com.example.laterlist.databinding.FragmentCreateWebsiteBinding

class CreateWebsiteFragment : BaseDialogFragment<FragmentCreateWebsiteBinding>(FragmentCreateWebsiteBinding::inflate) {
    private var customDialogCallback: MenuItemDialogClickCallBack<LaterViewItem>? = null

    companion object{
        fun newInstance(customDialogCallback: MenuItemDialogClickCallBack<LaterViewItem>): CreateWebsiteFragment {
            val fragment = CreateWebsiteFragment()
            fragment.customDialogCallback = customDialogCallback
            return fragment
        }
    }

    override fun onCreateView() { }

    override fun initClickListener() {
        // 注册取消的事件监听
        viewBinding.createWebsiteButtonCancel.setOnClickListener {
            // 执行自定义的取消事件
            customDialogCallback?.onCancelClickListener()
            onDismiss()
        }

        // 注册确定的事件监听
        viewBinding.createWebsiteButtonConfirm.setOnClickListener {
            val tags = mutableListOf<String>()
            val laterViewItem = LaterViewItem(
                title = viewBinding.createWebsiteTitleEv.text.toString(),
                contentUrl = viewBinding.createWebsiteUrlEv.text.toString(),
                itemType = ItemType.WEB_PAGE,
                folder = viewBinding.fragmentFolderTagSelector.folderHeaderSpinner.selectedItem.toString(),
                tag = tags,
                thumbnailUrl = "",
                content = viewBinding.createWebsiteContentEv.text.toString(),
                description = viewBinding.createWebsiteContentEv.text.toString(),
                isRead = false,
                isDelete = false,
                isTop = false
            )
            // 执行自定义的确定事件
            customDialogCallback?.onConfirmClickListener(laterViewItem)
            onDismiss()
        }
    }

    override fun focusWidget(): EditText? {
        return viewBinding.createWebsiteUrlEv
    }

    private fun onDismiss(){
        // TODO 清空内容，防止下次弹出显示
        // 取消弹出的对话框
        dismiss()
    }
}