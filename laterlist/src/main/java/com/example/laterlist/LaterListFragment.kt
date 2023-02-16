package com.example.laterlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import com.example.common.adapter.ViewPagerAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.utils.TheRouterUtil
import com.example.laterlist.alllater.AllLaterListFragment
import com.example.laterlist.databinding.FragmentLaterListBinding
import com.example.laterlist.tags.TagListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.LaterListFragment,
    description = "The entrance fragment of laterList"
)
class LaterListFragment : BaseFragment<FragmentLaterListBinding>(FragmentLaterListBinding::inflate) {
    private lateinit var fragments : Array<Fragment>

    private lateinit var titles: Array<String>

    private lateinit var icons: Array<Int?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView() {
        fragments= arrayOf(
            // add fragments here
            TheRouterUtil.getFragmentByPath<AllLaterListFragment>(RoutePathConstant.AllLaterListFragment) ?: AllLaterListFragment(),
            TheRouterUtil.getFragmentByPath<TagListFragment>(RoutePathConstant.TagListFragment) ?: TagListFragment()
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
    }

    private fun init(){
        // 设置顶部toolbar的点击事件
//        viewBinding.laterListToolbar.laterListToolbarMotionLayout.children.forEachIndexed { index, view ->
//            if (index != 0) (view as MotionLayout).transitionToEnd()
//            view.setOnClickListener {
//                viewBinding.laterListToolbar.laterListToolbarMotionLayout.children.forEachIndexed { _, temp ->
//                    if (temp.id != it.id) (temp as MotionLayout).transitionToEnd()
//                    else (temp as MotionLayout).transitionToStart()
//                }
//            }
//        }
        viewBinding.laterListViewPager.isUserInputEnabled = true
        // 配置页面下面的 viewPager 与 tabLayout
        viewBinding.laterListViewPager.adapter = ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)
        TabLayoutMediator(viewBinding.laterListTabLayout, viewBinding.laterListViewPager, true, true
        ) { tab: TabLayout.Tab, position: Int ->
            tab.customView = generateItemTab(position)
        }.attach()
    }

    private fun generateItemTab(position: Int): View{
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.tablayout_item_tab, null)
        val imageView = view.findViewById<ImageView>(R.id.item_icon)
        val textView = view.findViewById<TextView>(R.id.item_text)
        icons[position]?.let { imageView.setImageResource(it) }
        textView.text = titles[position]
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(com.example.common.R.menu.add_menu, menu)
    }
}