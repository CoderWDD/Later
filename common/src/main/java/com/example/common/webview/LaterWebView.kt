package com.example.common.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient


@SuppressLint("SetJavaScriptEnabled")
class LaterWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {
    private var isNightMode: Boolean = false

    private var onTitleChangeListener: OnTitleChangeListener? = null

    init {
        val webSettings = settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                applyNightMode()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.title?.let {
                    onTitleChangeListener?.onTitleChange(it)
                }
            }
        }
    }

    fun setTextSize(textSize: WebSettings.TextSize) {
        settings.textSize = textSize
    }

    fun toggleNightMode() {
        isNightMode = !isNightMode
        applyNightMode()
    }

    private fun applyNightMode() {
        if (isNightMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                settings.forceDark = WebSettings.FORCE_DARK_ON
            } else {
                evaluateJavascript(
                    """
                    (function() {
                        document.body.style.backgroundColor = '#000000';
                        document.body.style.color = '#ffffff';
                    })();
                    """.trimIndent(), null
                )
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                settings.forceDark = WebSettings.FORCE_DARK_OFF
            } else {
                evaluateJavascript(
                    """
                    (function() {
                        document.body.style.backgroundColor = '';
                        document.body.style.color = '';
                    })();
                    """.trimIndent(), null
                )
            }
        }
    }

    fun setOnTitleChangeListener(listener: OnTitleChangeListener) {
        onTitleChangeListener = listener
    }

    interface OnTitleChangeListener {
        fun onTitleChange(title: String)
    }

}