package com.example.laterlist.alllater

import android.annotation.SuppressLint
import android.view.View
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
import com.example.laterlist.R
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.AllLaterListFragment,
    description = "The sub fragment of later-list fragment"
)
class AllLaterListFragment : BaseFragment<FragmentAllLaterListBinding>(FragmentAllLaterListBinding::inflate) {
    private lateinit var categoryListFavoriteAdapter: RecyclerViewAdapter
    private lateinit var categoryMoreFavoriteAdapter: RecyclerViewAdapter

    override fun onCreateView() {
        initCategory()
        initCategoryListFavorite()
        initCategoryListMore()
        setOnCategoryListFavoriteItemClick()
        setCategoryHeadVisible()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initCategoryListFavorite(){
        val folderData = FolderData("folder_1", "12", icon = resources.getDrawable(com.example.common.R.drawable.folder_icon))
        val dateList = mutableListOf<Any>(folderData)
        repeat(14){dateList.add(folderData)}
        val folderCardProxy = FolderCardProxy()
        val proxyList = mutableListOf<RVProxy<*, *>>(folderCardProxy)
        categoryListFavoriteAdapter = RecyclerViewAdapter(dataList = dateList, proxyList = proxyList)
        viewBinding.categoryListFavorite.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.categoryListFavorite.categoryRecyclerView.addItemDecoration(RecyclerViewItemDecoration(requireContext(), RecyclerViewItemDecoration.VERTICAL, 48.dp.toInt(), 0.dp.toInt()))
        viewBinding.categoryListFavorite.categoryRecyclerView.adapter = categoryListFavoriteAdapter
    }

    private fun initCategoryListMore(){
        val folderData = FolderData("folder_1", "12", icon = resources.getDrawable(com.example.common.R.drawable.folder_icon))
        val dateList = mutableListOf<Any>(folderData)
        repeat(15){dateList.add(folderData)}
        val folderCardProxy = FolderCardProxy()
        val proxyList = mutableListOf<RVProxy<*, *>>(folderCardProxy)
        categoryMoreFavoriteAdapter = RecyclerViewAdapter(dataList = dateList, proxyList = proxyList)
        viewBinding.categoryListMore.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.categoryListMore.categoryRecyclerView.addItemDecoration(RecyclerViewItemDecoration(requireContext(), RecyclerViewItemDecoration.VERTICAL, 48.dp.toInt(), 0.dp.toInt()))
        viewBinding.categoryListMore.categoryRecyclerView.adapter = categoryMoreFavoriteAdapter
    }

    private fun setOnCategoryListFavoriteItemClick(){
        viewBinding.categoryListFavorite.categoryRecyclerView.setOnItemClickListener { view, position ->
            // 跳转到相应页面
        }
    }

    private fun setOnCategoryListMoreItemClick(){
        viewBinding.categoryListMore.categoryRecyclerView.setOnItemClickListener { view, position ->
            // 跳转到相应页面
        }
    }

    private fun setCategoryHeadVisible(){
        // 如果标题下面没有文件夹，就不显示标题
        if (categoryListFavoriteAdapter.dataList.size == 0) {
            viewBinding.categoryListFavorite.categoryHeaderTitle.visibility = View.GONE
        }else {
            viewBinding.categoryListFavorite.categoryHeaderTitle.visibility = View.VISIBLE
        }
        if (categoryMoreFavoriteAdapter.dataList.size == 0){
            viewBinding.categoryListMore.categoryHeaderTitle.visibility = View.GONE
        }else {
            viewBinding.categoryListMore.categoryHeaderTitle.visibility = View.VISIBLE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initCategory(){
        viewBinding.categoryPlate.categoryFavorite.categoryCnt.text = "0"
        viewBinding.categoryPlate.categoryFavorite.categoryIcon.setImageDrawable(resources.getDrawable(
            R.drawable.favorite_icon
        ))
        viewBinding.categoryPlate.categoryFavorite.categoryText.text = "收藏"

        viewBinding.categoryPlate.categoryToday.categoryCnt.text = "0"
        viewBinding.categoryPlate.categoryToday.categoryIcon.setImageDrawable(resources.getDrawable(com.example.common.R.drawable.today_icon))
        viewBinding.categoryPlate.categoryToday.categoryText.text = "今天"
    }
}