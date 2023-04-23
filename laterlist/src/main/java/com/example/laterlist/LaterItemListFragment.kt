package com.example.laterlist

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.entity.ItemType
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.LaterImageItemCardProxy
import com.example.common.recyclerview.proxy.LaterVideoItemCardProxy
import com.example.common.recyclerview.proxy.LaterWebsiteItemCardProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.common.reporesource.Resource
import com.example.common.utils.FragmentStackUtil
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
        initItemListClickListener()
        initToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.laterAllItemListToolBarTitle.text = "Item列表"
        viewBinding.laterAllItemListToolBarTitle.gravity = Gravity.CENTER
    }

    private fun initToolbar() {
        viewBinding.toolbar.navigationIcon = resources.getDrawable(com.example.common.R.drawable.baseline_arrow_back_24)
        viewBinding.toolbar.setNavigationOnClickListener {
            FragmentStackUtil.popBackStack(this)
//            FragmentStackUtil.navBack()
        }
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

    private fun initItemListClickListener() {
        viewBinding.laterItemList.setOnItemClickListener { view, position ->
            // todo 根据item类型跳转到不同的页面
        }


        viewBinding.laterItemList.setOnItemClickListener { view, position, fl, fl2 ->
            // set item click listener
            // star item
            view.findViewById<ImageView>(com.example.common.R.id.item_favorite).setOnClickListener {
                val laterItem = LaterViewItem.fromExitItem(laterItemList[position])
                LaterLog.d("laterItem $laterItem")
                viewModel.updateLaterItem(folderKey, laterItem.copy(isStar = !laterItem.isStar)).observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            LaterLog.d("updateLaterItem success")
                            laterItemListAdapter.notifyItemChanged(position)
                        }
                        is Resource.Error -> {
                            LaterLog.d("updateLaterItem error ${resource.message}")
                        }
                        is Resource.Loading -> {}
                        is Resource.Cached -> {}
                        else -> {}
                    }
                }
            }

            // todo share item
            view.findViewById<ImageView>(com.example.common.R.id.item_share).setOnClickListener {
                LaterLog.d("share item")
            }

            // todo delete item
            view.findViewById<ImageView>(com.example.common.R.id.item_more).setOnClickListener {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FragmentStackUtil.popBackStack(this)
    }
}