package com.example.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.custom.BaseFragment
import com.example.common.databinding.FragmentContainerBinding

class ContainerFragment : BaseFragment<FragmentContainerBinding>(FragmentContainerBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView() {
    }
}