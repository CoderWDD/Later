package com.example.laterlist.tags

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.LaterListType
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.dialogs.showDeleteDialog
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.TagCardData
import com.example.common.recyclerview.proxy.TagCardProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.common.recyclerview.setOnItemLongClickListener
import com.example.common.reporesource.Resource
import com.example.common.utils.FragmentStackUtil
import com.example.laterlist.databinding.FragmentTagListBinding
import com.example.common.viewmodel.LaterListViewModel
import com.example.laterlist.LaterItemListFragment
import com.therouter.TheRouter
import com.therouter.router.Route


@Route(
    path = RoutePathConstant.TagListFragment,
    description = "The sub fragment of later-list fragment"
)
class TagListFragment : BaseFragment<FragmentTagListBinding>(FragmentTagListBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
    private val tagList: MutableList<Any> = mutableListOf()
    private lateinit var tagListRecyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView() {
        initObjects()
        getDataFromViewModel()
        initTagListRecyclerView()
        setTagListRecyclerViewClickListener()
    }

    private fun initObjects(){
        viewModel = ViewModelProvider(requireActivity())[LaterListViewModel::class.java]
    }

    private fun getDataFromViewModel() {
        // get tag list from view model
        viewModel.getTagList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    tagList.clear()
                    resource.data.forEach { tag -> tagList.add(TagCardData(key = tag.key, icon = resources.getDrawable(com.example.laterlist.R.drawable.tag_icon), cnt = tag.laterViewItem.size.toString(), title = tag.name)) }
                    tagListRecyclerViewAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }
    }

    private fun initTagListRecyclerView(){
        val tagCardProxy = TagCardProxy()
        val proxyList = mutableListOf<RVProxy<*, *>>(tagCardProxy)
        tagListRecyclerViewAdapter = RecyclerViewAdapter(dataList = tagList, proxyList = proxyList)
        viewBinding.tagRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.tagRecyclerView.adapter = tagListRecyclerViewAdapter
    }

    private fun setTagListRecyclerViewClickListener(){
        viewBinding.tagRecyclerView.setOnItemLongClickListener { _, position ->
            LaterLog.d("setTagListRecyclerViewClickListener: $position")
            val tag = tagList[position] as TagCardData
            // 弹出删除对话框
            showDeleteDialog(context = requireContext(), title = "删除标签", content = "确定要删除该标签吗？", positiveText = "确定", negativeText = "取消", positiveListener = {
                // 删除收藏夹
                viewModel.deleteTag(tag = tag.key)
            }, negativeListener = {})
        }

        viewBinding.tagRecyclerView.setOnItemClickListener { view, position ->
            val laterItemListFragment = TheRouter.build(RoutePathConstant.LaterItemListFragment)
                .withString("folderKey", "")
                .withString("type", LaterListType.TAG.name)
                .withString("laterTag", (tagList[position] as TagCardData).key)
                .createFragment<LaterItemListFragment>()
            if (laterItemListFragment != null) {
                FragmentStackUtil.addFragment(laterItemListFragment)
            }
        }
    }
}