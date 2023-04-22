package com.example.laterlist.alllater

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.custom.RecyclerViewItemDecoration
import com.example.common.extents.dp
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.FolderCardProxy
import com.example.common.recyclerview.proxy.FolderData
import com.example.common.recyclerview.setOnItemClickListener
import com.example.laterlist.databinding.FragmentAllLaterListBinding
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.entity.LaterFolderEntity
import com.example.common.log.LaterLog
import com.example.common.recyclerview.setOnItemLongClickListener
import com.example.common.reporesource.Resource
import com.example.common.utils.TheRouterUtil
import com.example.laterlist.LaterItemListFragment
import com.example.laterlist.R
import com.example.laterlist.viewmodel.LaterListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.AllLaterListFragment,
    description = "The sub fragment of later-list fragment"
)
class AllLaterListFragment : BaseFragment<FragmentAllLaterListBinding>(FragmentAllLaterListBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
    private lateinit var favoriteFolderListAdapter: RecyclerViewAdapter
    private lateinit var recycleFolderListAdapter: RecyclerViewAdapter
    private val favoriteFolderList: MutableList<Any> = mutableListOf()
    private val recycleFolderList: MutableList<Any> = mutableListOf()

    override fun onCreateView() {
        initObjects()
        initFolderHeader()
        getDataFromViewModel()
        initFavoriteCategory(size = 0)
        initTodayCategory(size = 0)
        initFavoriteFolderList()
        initRecycleFolderList()
        setOnCategoryListFavoriteItemClick()
        setOnCategoryListMoreItemClick()
    }

    private fun initObjects(){
        viewModel = ViewModelProvider(requireActivity())[LaterListViewModel::class.java]
    }

    private fun initFolderHeader(){
        viewBinding.categoryListFavorite.categoryHeaderTitle.text = getString(R.string.favorite_category_header_title)
        viewBinding.categoryListMore.categoryHeaderTitle.text = getString(R.string.recycle_category_header_title)
    }

    private fun getDataFromViewModel(){
        // get favorite list
        viewModel.getFavoriteList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    initFavoriteCategory(resource.data.size)
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }

        // get today list
        viewModel.getTodayList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    initTodayCategory(resource.data.size)
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }

        // get favorite folder list
        viewModel.getFavoriteFolderList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    LaterLog.d("getFavoriteFolderList: ${resource.data}")
                    favoriteFolderList.clear()
                    resource.data.forEach { favoriteFolderList.add(FolderData(title = it.title, cnt = it.cnt.toString(), icon = resources.getDrawable(com.example.common.R.drawable.folder_icon), key = it.key)) }
                    favoriteFolderListAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }

        // get recycle folder list
        viewModel.getRecentFolderList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    recycleFolderList.clear()
                    resource.data.forEach { recycleFolderList.add(FolderData(title = it.title, cnt = it.cnt.toString(), icon = resources.getDrawable(com.example.common.R.drawable.folder_icon), key = it.key)) }
                    recycleFolderListAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Cached -> {}
                else -> {}
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initFavoriteFolderList(){
        val folderCardProxy = FolderCardProxy()
        val proxyList = mutableListOf<RVProxy<*, *>>(folderCardProxy)
        favoriteFolderListAdapter = RecyclerViewAdapter(dataList = favoriteFolderList, proxyList = proxyList)
        viewBinding.categoryListFavorite.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.categoryListFavorite.categoryRecyclerView.addItemDecoration(RecyclerViewItemDecoration(requireContext(), RecyclerViewItemDecoration.VERTICAL, 48.dp.toInt(), 0.dp.toInt()))
        viewBinding.categoryListFavorite.categoryRecyclerView.adapter = favoriteFolderListAdapter
    }

    private fun initRecycleFolderList(){
        val folderCardProxy = FolderCardProxy()
        val proxyList = mutableListOf<RVProxy<*, *>>(folderCardProxy)
        recycleFolderListAdapter = RecyclerViewAdapter(dataList = recycleFolderList, proxyList = proxyList)
        viewBinding.categoryListMore.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.categoryListMore.categoryRecyclerView.addItemDecoration(RecyclerViewItemDecoration(requireContext(), RecyclerViewItemDecoration.VERTICAL, 48.dp.toInt(), 0.dp.toInt()))
        viewBinding.categoryListMore.categoryRecyclerView.adapter = recycleFolderListAdapter
    }

    private fun setOnCategoryListFavoriteItemClick(){
        viewBinding.categoryListFavorite.categoryRecyclerView.setOnItemClickListener { _, position ->
            // 如果收藏夹中没有内容，则不跳转
            if ((favoriteFolderList[position] as FolderData).cnt == "0") return@setOnItemClickListener
            // 跳转到相应页面
            val laterItemListFragment = TheRouter.build(RoutePathConstant.LaterItemListFragment)
                .withString("folderKey", (favoriteFolderList[position] as FolderData).key)
                .createFragment<LaterItemListFragment>()
            if (laterItemListFragment != null) {
                TheRouterUtil.navToFragmentAdd<LaterItemListFragment>(fragment = laterItemListFragment, fragmentManager = requireActivity().supportFragmentManager)
            }
        }

        viewBinding.categoryListFavorite.categoryRecyclerView.setOnItemLongClickListener { _, position ->
            LaterLog.d("setOnCategoryListMoreItemClick: $position")
            // 弹出删除对话框
            showDeleteDialog(title = "删除收藏夹", content = "确定要删除收藏夹吗？", positiveText = "确定", negativeText = "取消", positiveListener = {
                // 删除收藏夹
                viewModel.deleteFavoriteFolder(folderKey = (favoriteFolderList[position] as FolderData).key )
            }, negativeListener = {})
        }
    }

    private fun setOnCategoryListMoreItemClick(){
        viewBinding.categoryListMore.categoryRecyclerView.setOnItemClickListener { _, position ->
            // 如果收藏夹中没有内容，则不跳转
            if ((recycleFolderList[position] as FolderData).cnt == "0") return@setOnItemClickListener
            // 跳转到相应页面
            val laterItemListFragment = TheRouter.build(RoutePathConstant.LaterItemListFragment)
                .withString("folderKey", (recycleFolderList[position] as FolderData).key)
                .createFragment<LaterItemListFragment>()
            if (laterItemListFragment != null) {
                TheRouterUtil.navToFragmentAdd<LaterItemListFragment>(fragment = laterItemListFragment, fragmentManager = requireActivity().supportFragmentManager)
            }
        }

        viewBinding.categoryListMore.categoryRecyclerView.setOnItemLongClickListener { _, position ->
            LaterLog.d("setOnCategoryListMoreItemClick: $position")
            // 弹出删除对话框
            showDeleteDialog(title = "删除回收站", content = "确定要删除回收站吗？", positiveText = "确定", negativeText = "取消", positiveListener = {
                // 删除回收站
                viewModel.deleteRecycleFolder(folderKey = (recycleFolderList[position] as FolderData).key )
            }, negativeListener = {})
        }
    }

    private fun setCategoryHeadVisible(){
        // 如果标题下面没有文件夹，就不显示标题
        if (favoriteFolderListAdapter.dataList.size == 0) {
            viewBinding.categoryListFavorite.categoryHeaderTitle.visibility = View.GONE
        }else {
            viewBinding.categoryListFavorite.categoryHeaderTitle.visibility = View.VISIBLE
        }
        if (recycleFolderListAdapter.dataList.size == 0){
            viewBinding.categoryListMore.categoryHeaderTitle.visibility = View.GONE
        }else {
            viewBinding.categoryListMore.categoryHeaderTitle.visibility = View.VISIBLE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initFavoriteCategory(size: Int){
        viewBinding.categoryPlate.categoryFavorite.categoryCnt.text = size.toString()
        viewBinding.categoryPlate.categoryFavorite.categoryIcon.setImageDrawable(resources.getDrawable(
            com.example.common.R.drawable.favorite_icon))
        viewBinding.categoryPlate.categoryFavorite.categoryText.text = "收藏"
        viewBinding.categoryPlate.categoryFavorite.root.setOnClickListener {  }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initTodayCategory(size: Int){
        viewBinding.categoryPlate.categoryToday.categoryCnt.text = size.toString()
        viewBinding.categoryPlate.categoryToday.categoryIcon.setImageDrawable(resources.getDrawable(com.example.common.R.drawable.today_icon))
        viewBinding.categoryPlate.categoryToday.categoryText.text = "今天"
        viewBinding.categoryPlate.categoryToday.root.setOnClickListener {  }
    }

    private fun showDeleteDialog(title: String, content: String, positiveText: String, negativeText: String, positiveListener: () -> Unit, negativeListener: () -> Unit) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton(positiveText) { dialog, which ->
                positiveListener()
                dialog.dismiss()
            }
            .setNegativeButton(negativeText) { dialog, which ->
                negativeListener()
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}