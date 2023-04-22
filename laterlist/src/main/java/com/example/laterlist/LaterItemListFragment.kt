package com.example.laterlist

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.entity.ItemType
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.LaterImageItemCardProxy
import com.example.common.recyclerview.proxy.LaterVideoItemCardProxy
import com.example.common.recyclerview.proxy.LaterWebsiteItemCardProxy
import com.example.common.reporesource.Resource
import com.example.common.utils.TheRouterUtil
import com.example.laterlist.databinding.FragmentLaterItemListBinding
import com.example.laterlist.viewmodel.LaterListViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.LaterItemListFragment,
    description = "The entrance fragment of later item list"
)
class LaterItemListFragment :
    BaseFragment<FragmentLaterItemListBinding>(FragmentLaterItemListBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
    private var laterItemList: MutableList<Any> = mutableListOf()
    private lateinit var laterItemListAdapter: RecyclerViewAdapter

    @Autowired(name = "folderKey")
    lateinit var folderKey: String

    override fun onCreateView() {
        viewModel = ViewModelProvider(requireActivity())[LaterListViewModel::class.java]
        initRecyclerViewAdapter()
        getInitData()
        initSwipeRefreshLayout()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.laterAllItemListToolBarTitle.text = "Item列表"
        viewBinding.laterAllItemListToolBarTitle.gravity = Gravity.CENTER
    }

    private fun getInitData() {
        viewModel.getListByFolder(folderKey).observe(viewLifecycleOwner) { resource ->
            viewBinding.swipeRefreshLayout.isRefreshing = false
            when (resource) {
                is Resource.Success -> {
                    laterItemList.clear()
                    resource.data.forEach {
                        when (it.itemType){
                            ItemType.IMAGE -> {
                                laterItemList.add(it.toImageItem())
                            }
                            ItemType.VIDEO -> {
                                laterItemList.add(it.toVideoItem())
                            }
                            ItemType.WEB_PAGE -> {
                                laterItemList.add(it.toWebPageItem())
                            }
                            ItemType.MUSIC -> {
                                laterItemList.add(it.toMusicItem())
                            }
                            ItemType.OTHER -> {
                                laterItemList.add(it.toOtherItem())
                            }
                        }
                    }
                    LaterLog.d("laterItemList $laterItemList")
                    laterItemListAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    LaterLog.d("error ${resource.message}")
                }
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }
    }

    private fun initRecyclerViewAdapter() {
        val proxyList = mutableListOf<RVProxy<*, *>>(
            LaterImageItemCardProxy(),
            LaterVideoItemCardProxy(),
            LaterWebsiteItemCardProxy()
        )
        laterItemListAdapter = RecyclerViewAdapter(proxyList = proxyList, dataList = laterItemList)
        viewBinding.laterItemList.adapter = laterItemListAdapter
    }

    private fun initSwipeRefreshLayout() {
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            getInitData()
        }
    }
}