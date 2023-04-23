package com.example.calendar

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.calendar.databinding.FragmentCalenderBinding
import com.example.common.adapter.ViewPagerAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.databinding.FragmentContainerBinding
import com.example.common.databinding.TablayoutItemTabBinding
import com.example.common.utils.TheRouterUtil
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.CalendarFragment,
    description = "The entrance fragment of calendar"
)
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(FragmentCalenderBinding::inflate) {

    override fun onCreateView() {
        initToolbar()
        initTodoRecyclerView()
        getData()
    }

    private fun getData(){

    }

    private fun initToolbar(){
        viewBinding.todoToolBarTitle.text = "待办"
        viewBinding.todoToolBarTitle.gravity = Gravity.CENTER
    }

    private fun initTodoRecyclerView(){

    }
}