package com.example.laterlist

import androidx.lifecycle.ViewModelProvider
import com.example.common.custom.BaseFragment
import com.example.laterlist.databinding.FragmentFolderTagBinding
import com.example.common.viewmodel.LaterListViewModel

class FolderTagFragment: BaseFragment<FragmentFolderTagBinding>(FragmentFolderTagBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel

    override fun onCreateView() {
        viewModel = ViewModelProvider(this)[LaterListViewModel::class.java]
        initFolderAndTags()
        initView()
    }

    private fun initFolderAndTags(){
        viewModel.getFavoriteFolderList()
        viewModel.getTagList()
    }

    private fun initView(){

    }

    private fun addChipToChipGroup(){ }

    private fun addSpinnerItem(){ }
}