package com.example.laterlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.laterlist.databinding.FragmentAllLaterListBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.AllLaterListFragment,
    description = "The sub fragment of later-list fragment"
)
class AllLaterListFragment : BaseFragment<FragmentAllLaterListBinding>(FragmentAllLaterListBinding::inflate) {

    override fun onCreateView() {
    }
}