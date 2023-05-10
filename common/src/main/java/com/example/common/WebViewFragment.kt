package com.example.common

import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.databinding.FragmentWebViewBinding
import com.example.common.log.LaterLog
import com.example.common.utils.FragmentStackUtil
import com.example.common.webview.LaterWebView
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(
    path = RoutePathConstant.WebViewFragment,
    description = "The entrance fragment of WebView"
)
class WebViewFragment : BaseFragment<FragmentWebViewBinding>(FragmentWebViewBinding::inflate), LaterWebView.OnTitleChangeListener {

    @Autowired
    lateinit var url: String

    override fun onCreateView() {
        initToolbar()
        viewBinding.webView.loadUrl(url)
    }

    private fun initToolbar(){
        viewBinding.webView.setOnTitleChangeListener(this)
        viewBinding.toolbarWebview.navigationIcon = resources.getDrawable(com.example.common.R.drawable.baseline_arrow_back_24)
        viewBinding.toolbarWebview.setNavigationOnClickListener {
            // 如果WebView可以返回上一页，则返回上一页，否则返回上一个Fragment
            if (viewBinding.webView.canGoBack()) viewBinding.webView.goBack()
            else {
                viewBinding.webView.clearData()
                FragmentStackUtil.goBack()
            }
        }
    }

    override fun onTitleChange(title: String) {
        viewBinding.webviewToolBarTitle.text = title
    }
}