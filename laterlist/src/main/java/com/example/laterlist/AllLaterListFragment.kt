package com.example.laterlist

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.FolderCardProxy
import com.example.common.recyclerview.proxy.FolderData
import com.example.common.recyclerview.proxy.FolderHeaderData
import com.example.common.recyclerview.proxy.FolderHeaderProxy
import com.example.laterlist.databinding.FragmentAllLaterListBinding
import com.example.laterlist.favorite.FavoriteRecyclerViewAdapter
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.AllLaterListFragment,
    description = "The sub fragment of later-list fragment"
)
class AllLaterListFragment : BaseFragment<FragmentAllLaterListBinding>(FragmentAllLaterListBinding::inflate) {
    lateinit var laterListAllRecyclerViewAdapter: FavoriteRecyclerViewAdapter

    override fun onCreateView() {
        viewBinding.laterListAllRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        initAdapter()
        viewBinding.laterListAllRecyclerView.adapter = laterListAllRecyclerViewAdapter
    }


    private fun initAdapter(){
        val folderHeaderProxy = FolderHeaderProxy()
        val folderCardProxy = FolderCardProxy()
        val folderHeaderData = FolderHeaderData("title_1")
//        laterListAllRecyclerViewAdapter.dataList.add(folderHeaderData)
        val folderData = FolderData("folder_1", "12")
//        laterListAllRecyclerViewAdapter.dataList.add(folderData)
        val dateList = mutableListOf<Any>(folderHeaderData, folderData)
        val proxyList = mutableListOf<RVProxy<*, *>>(folderCardProxy, folderHeaderProxy)
        laterListAllRecyclerViewAdapter = FavoriteRecyclerViewAdapter(dataList = dateList, proxyList = proxyList)
    }
}