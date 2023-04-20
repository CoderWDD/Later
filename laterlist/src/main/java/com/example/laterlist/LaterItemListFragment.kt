package com.example.laterlist

import androidx.lifecycle.ViewModelProvider
import com.example.common.R
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.recyclerview.proxy.FolderData
import com.example.common.reporesource.Resource
import com.example.laterlist.databinding.FragmentLaterItemListBinding
import com.example.laterlist.viewmodel.LaterListViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.LaterItemListFragment,
    description = "The entrance fragment of later item list"
)
class LaterItemListFragment : BaseFragment<FragmentLaterItemListBinding>(FragmentLaterItemListBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
    private lateinit var laterItemList: MutableList<LaterViewItem>

    @Autowired
    lateinit var folderKey: String

    companion object {

    }

    override fun onCreateView() {
        viewModel = ViewModelProvider(requireActivity())[LaterListViewModel::class.java]
        getInitData()
    }

    private fun getInitData(){
        viewModel.getListByFolder(folderKey).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    laterItemList.clear()
                    resource.data.forEach {
//                        laterItemList.add()
                    }
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }
    }

    private fun initRecyclerView() {
        viewBinding.laterItemList
    }
}