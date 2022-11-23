package com.example.laterlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.laterlist.databinding.FragmentLaterListBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.LaterListFragment,
    description = "The entrance fragment of laterList"
)
class LaterListFragment : BaseFragment<FragmentLaterListBinding>(FragmentLaterListBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {

    }
}