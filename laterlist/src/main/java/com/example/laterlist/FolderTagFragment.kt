package com.example.laterlist

import androidx.lifecycle.ViewModelProvider
import com.example.common.custom.BaseFragment
import com.example.laterlist.databinding.FragmentFolderTagBinding
import com.example.laterlist.viewmodel.LaterListViewModel

class FolderTagFragment: BaseFragment<FragmentFolderTagBinding>(FragmentFolderTagBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
    override fun onCreateView() {
        viewModel = ViewModelProvider(this)[LaterListViewModel::class.java]
        initFolderAndTags()
    }

    private fun initFolderAndTags(){
        viewModel.getFolderList()
        viewModel.getTagList()
    }
}