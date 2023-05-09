package com.example.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.databinding.FragmentWebViewBinding
import com.example.common.log.LaterLog
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.WebViewFragment,
    description = "The entrance fragment of WebView"
)
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(FragmentWebViewBinding::inflate) {

    @Autowired
    lateinit var url: String

    override fun onCreateView() {
        LaterLog.d(tag = "webview", message = url)
        viewBinding.webView.loadUrl(url)
    }
}