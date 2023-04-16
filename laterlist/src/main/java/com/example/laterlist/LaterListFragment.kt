package com.example.laterlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.adapter.ViewPagerAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.reporesource.Resource
import com.example.common.utils.TheRouterUtil
import com.example.laterlist.alllater.AllLaterListFragment
import com.example.laterlist.callback.MenuItemDialogClickCallBack
import com.example.laterlist.databinding.FragmentLaterListBinding
import com.example.laterlist.tags.TagListFragment
import com.example.laterlist.viewmodel.LaterListViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.therouter.router.Route
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@Route(
    path = RoutePathConstant.LaterListFragment,
    description = "The entrance fragment of laterList"
)
class LaterListFragment :
    BaseFragment<FragmentLaterListBinding>(FragmentLaterListBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel
    private lateinit var fragments: Array<Fragment>

    private lateinit var titles: Array<String>

    private lateinit var icons: Array<Int?>

    private lateinit var createFolderDialog: CreateFolderFragment

    private lateinit var createWebsiteDialog: CreateWebsiteFragment

    private lateinit var createTagDialog: CreateTagFragment

    override fun onCreateView() {
        viewModel = ViewModelProvider(this)[LaterListViewModel::class.java]
        fragments = arrayOf(
            // add fragments here
            TheRouterUtil.getFragmentByPath<AllLaterListFragment>(RoutePathConstant.AllLaterListFragment)
                ?: AllLaterListFragment(),
            TheRouterUtil.getFragmentByPath<TagListFragment>(RoutePathConstant.TagListFragment)
                ?: TagListFragment()
        )

        titles = arrayOf(
            // add tabLayout title here
            resources.getString(R.string.tab_layout_all_list),
            resources.getString(R.string.tab_layout_tag_list)
        )
        icons = arrayOf(
            null,
            R.drawable.tag_icon,
        )
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.laterAllToolBarTitle.text = "列表"
        viewBinding.laterAllToolBarTitle.gravity = Gravity.CENTER


    }

    private fun init() {
        // 初始化 toolbar
        initToolbar()
        // 初始化 viewPager
        initViewPager()
        // 初始化 menuItem 对应弹出的 dialog
        initMenuItemDialog()
        // 初始化 menuItem 的点击事件
        initMenuItemClick()
    }


    private fun generateItemTab(position: Int): View {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.tablayout_item_tab, null)
        val imageView = view.findViewById<ImageView>(R.id.item_icon)
        val textView = view.findViewById<TextView>(R.id.item_text)
        icons[position]?.let { imageView.setImageResource(it) }
        textView.text = titles[position]
        return view
    }

    private fun initToolbar() {
        // 设置顶部toolbar的点击事件
        viewBinding.laterListViewPager.isUserInputEnabled = true
    }

    private fun initViewPager() {
        // 配置页面下面的 viewPager 与 tabLayout
        viewBinding.laterListViewPager.adapter =
            ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)
        TabLayoutMediator(
            viewBinding.laterListTabLayout, viewBinding.laterListViewPager, true, true
        ) { tab: TabLayout.Tab, position: Int ->
            tab.customView = generateItemTab(position)
        }.attach()
    }

    // 根据不同的item，显示不同的添加页面
    private fun initMenuItemClick() {
        viewBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                com.example.common.R.id.create_folder -> {
                    createFolderDialog.show(
                        requireActivity().supportFragmentManager,
                        "create_folder"
                    )
                }
                com.example.common.R.id.create_website -> {
                    createWebsiteDialog.show(
                        requireActivity().supportFragmentManager,
                        "create_website"
                    )
                }
                com.example.common.R.id.create_tag -> {
                    createTagDialog.show(
                        requireActivity().supportFragmentManager,
                        "create_tag"
                    )
                }
                com.example.common.R.id.create_image -> {

                }
                com.example.common.R.id.create_video -> {

                }
            }
            true
        }
    }

    private fun initMenuItemDialog() {
        createFolderDialog = CreateFolderFragment.newInstance(object : MenuItemDialogClickCallBack<String> {
            override fun onConfirmClickListener(content: String) {
                // 执行创建文件夹的逻辑
                val folder = LaterFolderEntity(
                    title = content,
                    cnt = 0
                )
                createAndHandleFolder(folder)
            }

            override fun onCancelClickListener() {
                // 待加
            }
        })

        createWebsiteDialog = CreateWebsiteFragment.newInstance(object : MenuItemDialogClickCallBack<LaterViewItem> {
            override fun onConfirmClickListener(content: LaterViewItem) {
                // 执行创建文件夹的逻辑
                if (URLUtil.isValidUrl(content.contentUrl)) {
                    viewModel.createWebsite(content)
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("提示")
                        .setMessage("请输入正确的网址")
                        .setPositiveButton("确定") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }

            override fun onCancelClickListener() {
                // 待加
            }
        })

        createTagDialog = CreateTagFragment.newInstance(object : MenuItemDialogClickCallBack<String> {
            override fun onConfirmClickListener(content: String) {
                // 执行创建文件夹的逻辑
                createAndHandleTag(content)
            }

            override fun onCancelClickListener() {
                // 待加
            }
        })
    }

    private fun createAndHandleFolder(folder: LaterFolderEntity) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createFolder(folder).collect {
                    when (it) {
                        is Resource.Success -> {
                            // 创建文件夹成功
                            LaterLog.d("create folder success")
                        }
                        is Resource.Error -> {
                            // 创建文件夹失败
                            LaterLog.d("create folder failed")
                        }
                        is Resource.Loading -> {
                            // 创建文件夹中
                            LaterLog.d("create folder loading")
                        }
                        is Resource.Cached -> {
                            // 创建文件夹为空
                            LaterLog.d("create folder empty")
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun createAndHandleTag(tag: String) {
        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.createTag(tag).collect {
                    when (it) {
                        is Resource.Success -> {
                            // 创建Tag成功
                            LaterLog.d("create tag success")
                        }
                        is Resource.Error -> {
                            // 创建Tag失败
                            LaterLog.d("create tag failed")
                        }
                        is Resource.Loading -> {
                            // 创建Tag中
                            LaterLog.d("create tag loading")
                        }
                        is Resource.Cached -> {
                            // 创建Tag为空
                            LaterLog.d("create tag empty")
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun createAndHandleWebsite(website: LaterViewItem) {
    }
}