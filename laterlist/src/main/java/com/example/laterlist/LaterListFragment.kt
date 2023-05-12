package com.example.laterlist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.URLUtil
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.adapter.ViewPagerAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.databinding.TablayoutItemTabBinding
import com.example.common.entity.ItemType
import com.example.common.entity.LaterFolderEntity
import com.example.common.entity.LaterTagEntity
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.recyclerview.proxy.FolderData
import com.example.common.reporesource.Resource
import com.example.common.utils.TheRouterUtil
import com.example.laterlist.alllater.AllLaterListFragment
import com.example.common.callback.MenuItemDialogClickCallBack
import com.example.common.extents.showToast
import com.example.laterlist.databinding.FragmentLaterListBinding
import com.example.laterlist.tags.TagListFragment
import com.example.common.viewmodel.LaterListViewModel
import com.example.imagepicker.ImagePickerActivity
import com.example.imagepicker.constants.ResultCode
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.therouter.router.Route
import kotlinx.coroutines.*

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

    private val folderList: MutableList<FolderData> = mutableListOf()

    private val tagList: MutableList<LaterTagEntity> = mutableListOf()

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView() {
        viewModel = ViewModelProvider(requireActivity())[LaterListViewModel::class.java]
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
        // 初始化数据
        observeData()
        initActivityLauncher()
        // 初始化 toolbar
        initToolbar()
        // 初始化 viewPager
        initViewPager()
        // 初始化 menuItem 对应弹出的 dialog
        initMenuItemDialog()
        // 初始化 menuItem 的点击事件
        initMenuItemClick()
    }

    private fun initActivityLauncher(){
        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ResultCode.Image.OK) {
                val data = result.data
                val url = data?.getStringExtra("url")
                if (url != null && URLUtil.isValidUrl(url)) {
//                    viewModel.insertLaterItem(LaterViewItem(ItemType.Website, url))
                } else {
                    showToast("url is invalid")
                }
            }
        }
    }

    private fun observeData(){
        viewModel.sharedAction.observe(viewLifecycleOwner){
            when (it.itemType) {
                ItemType.WEB_PAGE -> {
                    createWebsiteDialog.setLaterViewItem(it)
                    createWebsiteDialog.show(requireActivity().supportFragmentManager, "createWebsiteDialog")
                }
                ItemType.IMAGE -> {
                    createFolderDialog.show(requireActivity().supportFragmentManager, "createFolderDialog")
                }
                ItemType.VIDEO -> {
                    createTagDialog.show(requireActivity().supportFragmentManager, "createTagDialog")
                }
                ItemType.MUSIC -> {}
                ItemType.OTHER -> {}
            }
        }

        viewModel.getFavoriteFolderList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    folderList.clear()
                    resource.data.forEach { folderList.add(FolderData(title = it.title, cnt = it.cnt.toString(), icon = resources.getDrawable(com.example.common.R.drawable.folder_icon), key = it.key)) }
                    createWebsiteDialog.setFolderList(folderList)
                }

                is Resource.Error -> {
                    // 待加
                }

                is Resource.Loading -> {
                    // 待加
                }

                else -> {}
            }
        }

        viewModel.getTagList().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    tagList.clear()
                    tagList.addAll(resource.data)
                    createWebsiteDialog.setTagList(tagList)
                }

                is Resource.Error -> {
                    // 待加
                }

                is Resource.Loading -> {
                    // 待加
                }

                else -> {}
            }
        }
    }

    private fun generateItemTab(position: Int): View {
        val tabLayoutItemTabBinding = TablayoutItemTabBinding.inflate(layoutInflater)
        val imageView = tabLayoutItemTabBinding.itemIcon
        val textView = tabLayoutItemTabBinding.itemText
        icons[position]?.let { imageView.setImageResource(it) }
        textView.text = titles[position]
        return tabLayoutItemTabBinding.root
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
                    activityLauncher.launch(Intent(requireContext(), ImagePickerActivity::class.java))
                }
                com.example.common.R.id.create_video -> {

                }
            }
            true
        }
    }

    private fun initMenuItemDialog() {
        createFolderDialog = CreateFolderFragment.newInstance(object :
            MenuItemDialogClickCallBack<String> {
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

        createWebsiteDialog = CreateWebsiteFragment.newInstance(object :
            MenuItemDialogClickCallBack<LaterViewItem> {
            override fun onConfirmClickListener(content: LaterViewItem) {
                if (!checkIfTheTagAndFolderAllExist()) {
                    showToast("请先选择文件夹和标签")
                    return
                }
                // 执行创建网页的逻辑
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
        }, folderList = folderList, tagList = tagList)

        createTagDialog = CreateTagFragment.newInstance(object :
            MenuItemDialogClickCallBack<String> {
            override fun onConfirmClickListener(content: String) {
                // 执行创建文件夹的逻辑
                createAndHandleTag(content)
            }

            override fun onCancelClickListener() {
                // 待加
            }
        })
    }

    private fun checkIfTheTagAndFolderAllExist(): Boolean {
        return tagList.isNotEmpty() && folderList.isNotEmpty()
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
                viewModel.createTag(LaterTagEntity(name = tag)).collect {
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