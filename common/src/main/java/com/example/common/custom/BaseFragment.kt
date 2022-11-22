package com.example.common.custom

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.therouter.TheRouter

class BaseFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheRouter.inject(this)
    }
}