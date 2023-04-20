package com.example.laterlist

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.view.allViews
import com.example.common.custom.BaseDialogFragment
import com.example.common.entity.ItemType
import com.example.common.entity.LaterTagEntity
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.recyclerview.proxy.FolderData
import com.example.laterlist.callback.MenuItemDialogClickCallBack
import com.example.laterlist.chip.TagChip
import com.example.laterlist.databinding.FragmentCreateWebsiteBinding
import com.example.laterlist.spinner.FolderSpinnerAdapter
import com.google.android.material.chip.Chip

class CreateWebsiteFragment : BaseDialogFragment<FragmentCreateWebsiteBinding>(FragmentCreateWebsiteBinding::inflate) {
    private var customDialogCallback: MenuItemDialogClickCallBack<LaterViewItem>? = null
    private val mTagList: MutableList<LaterTagEntity> = mutableListOf()
    private var selectedTagList: MutableList<LaterTagEntity> = mutableListOf()

    companion object{
        private val mFolderList: MutableList<FolderData> = mutableListOf()
        fun newInstance(
            customDialogCallback: MenuItemDialogClickCallBack<LaterViewItem>,
            folderList: MutableList<FolderData>,
            tagList: MutableList<LaterTagEntity>
        ): CreateWebsiteFragment {
            val fragment = CreateWebsiteFragment()
            fragment.apply {
                this.customDialogCallback = customDialogCallback
                mFolderList.addAll(folderList)
                mTagList.addAll(tagList)
            }
            return fragment
        }
    }

    override fun onCreateView() {
        initFolderSelector(mFolderList)
        initTagSelector(mTagList)
    }

    private fun initFolderSelector(folderList: List<FolderData>) {
        // 初始化文件夹选择器
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.folder_spinner_item, folderList.map { it.title })
        spinnerAdapter.setDropDownViewResource(R.layout.folder_spinner_item)
        viewBinding.fragmentFolderTagSelector.folderHeaderSpinner.adapter = spinnerAdapter
    }

    private fun initTagSelector(tagList: List<LaterTagEntity>){
        // 初始化标签选择器
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.folder_spinner_item, tagList.map { it.name })
        spinnerAdapter.setDropDownViewResource(R.layout.folder_spinner_item)
        viewBinding.fragmentFolderTagSelector.tagHeaderSpinner.adapter = spinnerAdapter
        viewBinding.fragmentFolderTagSelector.tagHeaderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                LaterLog.d("onItemSelected: $position")
                // 将选中的标签添加到标签列表中
                val tag = tagList[position]
                val tagChip = TagChip(requireContext())
                tagChip.text = tag.name
                tagChip.setOnCloseIconClickListener {
                    viewBinding.createWebsiteTagChipGroup.removeView(it)
                    if (it is TagChip) {
                        selectedTagList.removeIf { selectedTag -> selectedTag.name == it.text }
                    }
                }
                viewBinding.createWebsiteTagChipGroup.addView(tagChip)
                selectedTagList.add(tag)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                LaterLog.d("onNothingSelected")
            }
        }
    }

    override fun initClickListener() {
        // 注册取消的事件监听
        viewBinding.createWebsiteButtonCancel.setOnClickListener {
            // 执行自定义的取消事件
            customDialogCallback?.onCancelClickListener()
            onDismiss()
        }

        // 注册确定的事件监听
        viewBinding.createWebsiteButtonConfirm.setOnClickListener {
            val folderPosition = viewBinding.fragmentFolderTagSelector.folderHeaderSpinner.selectedItemPosition
            val folderPath = mFolderList[folderPosition].key
            val tags = mutableListOf<String>()
            tags.addAll(selectedTagList.map { it.name })
            val laterViewItem = LaterViewItem(
                title = viewBinding.createWebsiteTitleEv.text.toString(),
                contentUrl = viewBinding.createWebsiteUrlEv.text.toString(),
                itemType = ItemType.WEB_PAGE,
                folder = folderPath,
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

    fun setFolderList(folderList: List<FolderData>){
        mFolderList.clear()
        mFolderList.addAll(folderList)
    }

    fun setTagList(tagList: List<LaterTagEntity>){
        mTagList.clear()
        mTagList.addAll(tagList)
    }
}