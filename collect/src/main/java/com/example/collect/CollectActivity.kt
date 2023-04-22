package com.example.collect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.collect.databinding.ActivityCollectBinding
import com.example.common.log.LaterLog
import com.example.common.utils.StringUtils

class CollectActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCollectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityCollectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_collect)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Handle different actions separately
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type || "text/html" == intent.type) {
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
        var title:String
        var url: String
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



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_collect, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_collect)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}