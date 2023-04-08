package com.example.laterlist

import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.laterlist.databinding.FragmentCollectLaterListBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.CollectLaterListFragment,
    description = "The sub fragment of later-list fragment"
)
class CollectLaterListFragment : BaseFragment<FragmentCollectLaterListBinding>(FragmentCollectLaterListBinding::inflate) {
    override fun onCreateView() {

    }
}