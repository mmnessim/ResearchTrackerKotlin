package com.mnessim.researchtrackerkmp.utils.notifications

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.NotificationManager as AndroidNotificationManager

actual class NotificationManager actual constructor() {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    actual fun showNotification(title: String, message: String) {
        val channelId = "default_channel"
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default",
                AndroidNotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        manager.notify(1, notification)
    }
}