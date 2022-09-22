package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService


private val NOTIFICATION_ID = 0
fun createChannel(
    context: Context,
    channelId: String,
    channelName: String,
    description: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create channel to show notifications.
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                this.description = description
                setShowBadge(false)
            }

        val notificationManager = getSystemService(
            context,
            NotificationManager::class.java
        )
        notificationManager?.createNotificationChannel(notificationChannel)

    }
}

fun NotificationManager.sendNotification(
    applicationContext: Context,
    content: String,
    fileName: String,
    state: Boolean
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val detailsIntent = Intent(applicationContext, DetailActivity::class.java)
    detailsIntent.putExtra(MainActivity.FILE_NAME, fileName)
    detailsIntent.putExtra(MainActivity.DOWNLOAD_STATUS, state)

    val detailsPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailsIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )



    val builder = NotificationCompat.Builder(applicationContext, MainActivity.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(content)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_launcher_foreground, "Check the status", detailsPendingIntent)
        .setAutoCancel(true)
    notify(NOTIFICATION_ID, builder.build())
}

fun clearNotifications(applicationContext: Context) {
    val notificationManager = NotificationManagerCompat.from(applicationContext)
    notificationManager.cancelAll()
}