package com.example.laterlist

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.common.WebViewFragment
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.custom.customView.InterceptEventImageView
import com.example.common.dialogs.showDeleteDialog
import com.example.common.entity.ItemType
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.LaterImageItemCardProxy
import com.example.common.recyclerview.proxy.LaterVideoItemCardProxy
import com.example.common.recyclerview.proxy.LaterWebsiteItemCardProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.common.recyclerview.setOnItemLongClickListener
import com.example.common.reporesource.Resource
import com.example.common.utils.FragmentStackUtil
import com.example.laterlist.databinding.FragmentLaterItemListBinding
import com.example.common.viewmodel.LaterListViewModel
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.LaterItemListFragment,
    description = "The entrance fragment of later item list"
)
class LaterItemListFragment :
    BaseFragment<FragmentLaterItemListBinding>(FragmentLaterItemListBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
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
            FragmentStackUtil.goBack()
        }
    }

    private fun getInitData() {
        viewModel.getListByFolder(folderKey).observe(viewLifecycleOwner) { resource ->
            viewBinding.swipeRefreshLayout.isRefreshing = false
            when (resource) {
                is Resource.Success -> {
                    laterItemListAdapter.deleteAllData()
                    resource.data.forEach {
                        when (it.itemType){
                            ItemType.IMAGE -> {
                                laterItemListAdapter.addData(it.toImageItem())
                            }
                            ItemType.VIDEO -> {
                                laterItemListAdapter.addData(it.toVideoItem())
                            }
                            ItemType.WEB_PAGE -> {
                                laterItemListAdapter.addData(it.toWebPageItem())
                            }
                            ItemType.MUSIC -> {
                                laterItemListAdapter.addData(it.toMusicItem())
                            }
                            ItemType.OTHER -> {
                                laterItemListAdapter.addData(it.toOtherItem())
                            }
                        }
                    }
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
        laterItemListAdapter = RecyclerViewAdapter(proxyList = proxyList)
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
            val laterItem = LaterViewItem.fromExitItem(laterItemListAdapter.dataList[position])
            LaterLog.d("laterItem $laterItem", tag = "com/example/common/webview")
            if (laterItem.itemType == ItemType.WEB_PAGE) {
                val laterItemListFragment = TheRouter.build(RoutePathConstant.WebViewFragment)
                    .withString("url", laterItem.contentUrl)
                    .withSerializable("laterItem", laterItem)
                    .withString("folderKey", folderKey)
                    .createFragment<WebViewFragment>()
                laterItemListFragment?.let { FragmentStackUtil.addFragment(it) }
            }
        }

        viewBinding.laterItemList.setOnItemLongClickListener { view, position ->
            showDeleteDialog(requireContext(), "是否删除", "确定删除该Item吗？", positiveText = "确定", negativeText = "取消", negativeListener = {}, positiveListener =  {
                val laterItem = LaterViewItem.fromExitItem(laterItemListAdapter.dataList[position])
                viewModel.deleteLaterItem(folderKey, laterItem)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}