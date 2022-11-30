package com.example.laterlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.log.LaterLog
import com.example.laterlist.databinding.FragmentAllLaterListBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.AllLaterListFragment,
    description = "The sub fragment of later-list fragment"
)
class AllLaterListFragment : BaseFragment<FragmentAllLaterListBinding>(FragmentAllLaterListBinding::inflate) {

    @SuppressLint("InflateParams")
    override fun onCreateView() {

    }
}