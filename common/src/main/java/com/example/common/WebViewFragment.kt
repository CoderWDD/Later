package com.example.common

import androidx.lifecycle.ViewModelProvider
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseFragment
import com.example.common.databinding.FragmentWebViewBinding
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.reporesource.Resource
import com.example.common.utils.FragmentStackUtil
import com.example.common.viewmodel.LaterListViewModel
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

    @Autowired
    lateinit var laterItem: LaterViewItem

    @Autowired
    lateinit var folderKey: String

    private lateinit var viewModel: LaterListViewModel

    private var isStar = false

    override fun onCreateView() {
        viewModel = ViewModelProvider(requireActivity())[LaterListViewModel::class.java]
        isStar = laterItem.isStar
        initToolbar()
        initClickListener()
        viewBinding.webView.loadUrl(url)
    }

    private fun initClickListener(){
        viewBinding.toolbarIconStar.setOnClickListener {
            viewModel.updateLaterItem(folderKey, laterItem.copy(isStar = !isStar)).observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        LaterLog.d("updateLaterItem success")
                        isStar = !isStar
                        updateStarIcon(isStar)
                    }
                    is Resource.Error -> {
                        LaterLog.d("updateLaterItem error ${resource.message}")
                    }
                    is Resource.Loading -> {}
                    is Resource.Cached -> {}
                    else -> {}
                }
            }
        }
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
        updateStarIcon(isStar)
    }

    private fun updateStarIcon(star: Boolean) {
        if (star) viewBinding.toolbarIconStar.setImageResource(R.drawable.favorite_active)
        else viewBinding.toolbarIconStar.setImageResource(R.drawable.favorite_inactive)
    }

    override fun onTitleChange(title: String) {
        viewBinding.webviewToolBarTitle.text = title
    }
}