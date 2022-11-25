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
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.laterlist.databinding.FragmentLaterListBinding
import com.therouter.router.Route
import kotlin.math.log

@Route(
    path = RoutePathConstant.LaterListFragment,
    description = "The entrance fragment of laterList"
)
class LaterListFragment : BaseFragment<FragmentLaterListBinding>(FragmentLaterListBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
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
    }
}