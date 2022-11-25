package com.example.laterlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.transition.Visibility
import com.example.common.adapter.ViewPagerAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.utils.TheRouterUtil
import com.example.laterlist.databinding.FragmentLaterListBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.therouter.router.Route
import kotlin.math.log

@Route(
    path = RoutePathConstant.LaterListFragment,
    description = "The entrance fragment of laterList"
)
class LaterListFragment : BaseFragment<FragmentLaterListBinding>(FragmentLaterListBinding::inflate) {
    private lateinit var fragments : Array<Fragment>

    private lateinit var titles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
        fragments= arrayOf(
            // add fragments here
            TheRouterUtil.getFragmentByPath<AllLaterListFragment>(RoutePathConstant.AllLaterListFragment) ?: AllLaterListFragment(),
            TheRouterUtil.getFragmentByPath<CollectLaterListFragment>(RoutePathConstant.CollectLaterListFragment) ?: CollectLaterListFragment(),
            TheRouterUtil.getFragmentByPath<TagListFragment>(RoutePathConstant.TagListFragment) ?: TagListFragment()
        )

        titles = arrayOf(
            // add tabLayout title here
            resources.getString(R.string.tab_layout_all_list),
            resources.getString(R.string.tab_layout_collect_list),
            resources.getString(R.string.tab_layout_tag_list)
        )
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init(){
        // 设置顶部toolbar的点击事件
        viewBinding.laterListToolbar.laterListToolbarMotionLayout.children.forEachIndexed { index, view ->
            if (index != 0) (view as MotionLayout).transitionToEnd()
            view.setOnClickListener {
                viewBinding.laterListToolbar.laterListToolbarMotionLayout.children.forEachIndexed { _, temp ->
                    if (temp.id != it.id) (temp as MotionLayout).transitionToEnd()
                    else (temp as MotionLayout).transitionToStart()
                }
            }
        }
        viewBinding.laterListViewPager.isUserInputEnabled = true
        // 配置页面下面的 viewPager 与 tabLayout
        viewBinding.laterListViewPager.adapter = ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)
        TabLayoutMediator(viewBinding.laterListTabLayout, viewBinding.laterListViewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }
}