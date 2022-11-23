package com.example.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.databinding.FragmentContainerBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.CalendarFragment,
    description = "The entrance fragment of calendar"
)
class CalenderFragment : BaseFragment<FragmentContainerBinding>(FragmentContainerBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
    }
}