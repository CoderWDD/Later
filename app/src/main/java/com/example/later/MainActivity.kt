package com.example.later

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.ViewModelProvider
import com.example.common.constants.RoutePathConstant
import com.example.common.custom.BaseActivity
import com.example.common.entity.ItemType
import com.example.common.entity.LaterViewItem
import com.example.common.log.LaterLog
import com.example.common.utils.TheRouterUtil
import com.example.home.HomeFragment
import com.example.later.databinding.ActivityMainBinding
import com.example.laterlist.viewmodel.LaterListViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var viewModel: LaterListViewModel

    override fun onCreate() {
        TheRouterUtil.navToFragmentAdd<HomeFragment>(RoutePathConstant.HomeFragment, supportFragmentManager)
        viewModel = ViewModelProvider(this)[LaterListViewModel::class.java]
        handleActionSend(intent)
    }

    private fun handleActionSend(intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (("text/plain" == intent.type) || ("text/html" == intent.type)) {
                    handleSendText(intent) // Handle text being sent
                } else if (intent.type?.startsWith("image/") == true) {
                    handleSendImage(intent) // Handle single image being sent
                } else if (intent.type?.startsWith("video/") == true) {
                    handleSendVideo(intent) // Handle single video being sent
                } else if (intent.type?.startsWith("audio/") == true) {
                    handleSendAudio(intent) // Handle single audio being sent
                } else if (intent.type?.startsWith("application/") == true) {
                    handleSendApplication(intent) // Handle single application being sent
                }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                if (intent.type?.startsWith("image/") == true){
                    handleSendMultipleImages(intent) // Handle multiple images being sent
                }else if (intent.type?.startsWith("video/") == true){
                    handleSendMultipleVideo(intent) // Handle multiple video being sent
                }else if (intent.type?.startsWith("audio/") == true) {
                    handleSendMultipleAudio(intent) // Handle multiple audio being sent
                }else if (intent.type?.startsWith("application/") == true) {
                    handleSendMultipleApplication(intent) // Handle multiple application being sent
                }
            }
            else -> {
                // Handle other intents, such as being started from the home screen
            }
        }
    }

    private fun handleSendMultipleApplication(intent: Intent) {

    }

    private fun handleSendMultipleAudio(intent: Intent) {

    }

    private fun handleSendApplication(intent: Intent) {

    }

    private fun handleSendAudio(intent: Intent) {

    }

    private fun handleSendText(intent: Intent) {
        var title:String = ""
        var url: String = ""
        intent.extras?.keySet()?.forEach { key ->
            when (key){
                Intent.EXTRA_SUBJECT -> {
                    title = intent.getStringExtra(key) ?: ""
                    LaterLog.d("title: $title")
                }
                Intent.EXTRA_TEXT -> {
                    url = intent.getStringExtra(key) ?: ""
                    LaterLog.d("url: $url")
                }
            }
        }
        val laterViewItem = LaterViewItem(
            title = title,
            contentUrl = url,
            itemType = ItemType.WEB_PAGE
        )
        viewModel.emitSharedAction(laterViewItem)
    }

    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
        }
    }

    private fun handleSendVideo(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
        }
    }

    private fun handleSendMultipleImages(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            // Update UI to reflect multiple images being shared
        }
    }

    private fun handleSendMultipleVideo(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            // Update UI to reflect multiple images being shared
        }
    }
}