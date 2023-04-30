package com.example.notification

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.common.adapter.RecyclerViewAdapter
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.recyclerview.RVProxy
import com.example.common.recyclerview.proxy.MsgItemProxy
import com.example.common.recyclerview.setOnItemClickListener
import com.example.notification.databinding.FragmentNotificationBinding
import com.example.notification.viewmodel.MsgViewModel
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.NotificationFragment,
    description = "The entrance fragment of notification"
)
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    private lateinit var viewModel: MsgViewModel
    private lateinit var msgRVAdapter: RecyclerViewAdapter
    private val msgList = mutableListOf<Any>()

    override fun onCreateView() {
        initObject()
        initToolbar()
        initClickListener()
    }

    private fun initClickListener(){
        viewBinding.chatFab.setOnClickListener {
            // switch to a new chat fragment
        }

        viewBinding.chatList.setOnItemClickListener { view, position ->
            // switch to chat fragment
        }

        viewBinding.chatList.setOnItemClickListener { view, position, fl, fl2 ->
            view.findViewById<ImageView>(com.example.common.R.id.delete_icon).setOnClickListener {
                // delete the item
            }

            view.findViewById<ImageView>(com.example.common.R.id.edit_icon).setOnClickListener {
                // edit the item
            }
        }
    }

    private fun initObject(){
        viewModel = ViewModelProvider(this)[MsgViewModel::class.java]
        val msgItemProxy = mutableListOf<RVProxy<*, *>>(MsgItemProxy())
        msgRVAdapter = RecyclerViewAdapter(msgItemProxy, msgList)
    }

    private fun initToolbar(){
        viewBinding.msgToolbarTitle.text = "消息"
        viewBinding.msgToolbarTitle.gravity = Gravity.CENTER
    }
}