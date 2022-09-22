package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {


    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var radioGroup: RadioGroup
    private var fileName: String = ""
    private var fileUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(this, CHANNEL_ID, CHANNEL_NAME, getString(R.string.notification_description3))

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radioGroup = findViewById(R.id.rg)

        custom_button.setOnClickListener {
            clearNotifications(this)
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                val notificationManager = ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager

                val selectedButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                when (selectedButton.text.substring(0, 1)) {
                    "G" -> {
                        notificationManager.sendNotification(
                            applicationContext,
                            getString(R.string.notification_description1), fileName , true
                        )
                    }
                    "L" -> {
                        notificationManager.sendNotification(
                            applicationContext,
                            getString(R.string.notification_description2), fileName , true
                        )
                    }
                    else -> {
                        notificationManager.sendNotification(
                            applicationContext,
                            getString(R.string.notification_description3), fileName , true
                        )
                    }
                }

            }
        }
    }

    private fun download() {
        if (rg.checkedRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.null_alert), Toast.LENGTH_SHORT).show()
        } else {
            val selectedButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
            fileName = selectedButton.text.toString()
            fileUrl = when (selectedButton.text.substring(0, 1)) {
                "G" -> {
                    "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
                }
                "L" -> {
                    "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
                }
                else -> {
                    "https://github.com/square/retrofit/archive/refs/heads/master.zip"
                }
            }
            val request =
                DownloadManager.Request(Uri.parse(fileUrl))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }

    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "Download Status"
        const val FILE_NAME = "fileName"
        const val DOWNLOAD_STATUS = "downloadStatus"

    }

}
