package com.example.home

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.calendar.CalenderFragment
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.utils.TheRouterUtil
import com.example.home.databinding.FragmentHomeBinding
import com.example.laterlist.LaterListFragment
import com.example.notification.NotificationFragment
import com.example.profile.ProfileFragment
import com.therouter.TheRouter
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.HomeFragment,
    description = "The fragment used to display home view"
)
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val fragments = listOf(
        TheRouterUtil.getFragmentByPath<LaterListFragment>(RoutePathConstant.LaterListFragment) as Fragment,
        TheRouterUtil.getFragmentByPath<CalenderFragment>(RoutePathConstant.CalendarFragment) as Fragment,
        TheRouterUtil.getFragmentByPath<NotificationFragment>(RoutePathConstant.NotificationFragment) as Fragment,
        TheRouterUtil.getFragmentByPath<ProfileFragment>(RoutePathConstant.ProfileFragment) as Fragment,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
        val viewPager2 = viewBinding.homeViewpage
        viewPager2.adapter = ViewPagerAdapter(fragments, requireActivity().supportFragmentManager, lifecycle)
        viewPager2.isUserInputEnabled = false // 禁止左右滑动
        viewBinding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.title) {
                getString(R.string.later_list_title) -> viewPager2.currentItem = 0
                getString(R.string.calendar_title) -> viewPager2.currentItem = 1
                getString(R.string.notification_title) -> viewPager2.currentItem = 2
                getString(R.string.profile_title) -> viewPager2.currentItem = 3
            }
            true
        }
    }
}