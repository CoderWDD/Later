package com.example.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.notification.databinding.FragmentNotificationBinding
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.NotificationFragment,
    description = "The entrance fragment of notification"
)
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
    }
}