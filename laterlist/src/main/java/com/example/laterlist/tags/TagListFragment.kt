package com.example.laterlist.tags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.R
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.custom.RecyclerViewItemDecoration
import com.example.common.extents.dp
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.FolderCardProxy
import com.example.common.recyclerview.proxy.FolderData
import com.example.common.recyclerview.proxy.TagCardProxy
import com.example.laterlist.databinding.FragmentTagListBinding
import com.therouter.router.Route


@Route(
    path = RoutePathConstant.TagListFragment,
    description = "The sub fragment of later-list fragment"
)
class TagListFragment : BaseFragment<FragmentTagListBinding>(FragmentTagListBinding::inflate) {
    private lateinit var tagListRecyclerViewAdapter: RecyclerViewAdapter
    override fun onCreateView() {
        initTagListRecyclerView()
        setTagListRecyclerViewClickListener()
    }

    private fun initTagListRecyclerView(){
        val folderData = FolderData("folder_1", "12", icon = resources.getDrawable(R.drawable.folder_icon))
        val dateList = mutableListOf<Any>(folderData)
        repeat(4){dateList.add(folderData)}
        val tagCardProxy = TagCardProxy()
        val proxyList = mutableListOf<RVProxy<*, *>>(tagCardProxy)
        tagListRecyclerViewAdapter = RecyclerViewAdapter(dataList = dateList, proxyList = proxyList)
        viewBinding.tagRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.tagRecyclerView.addItemDecoration(
            RecyclerViewItemDecoration(requireContext(), RecyclerViewItemDecoration.VERTICAL, 48.dp.toInt(), 0.dp.toInt())
        )
        viewBinding.tagRecyclerView.adapter = tagListRecyclerViewAdapter
    }

    private fun setTagListRecyclerViewClickListener(){

    }
}