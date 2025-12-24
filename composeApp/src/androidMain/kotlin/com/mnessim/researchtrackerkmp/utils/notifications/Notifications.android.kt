package com.mnessim.researchtrackerkmp.utils.notifications

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mnessim.researchtrackerkmp.MainActivity
import android.app.NotificationManager as AndroidNotificationManager

actual class NotificationManager actual constructor() {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    actual fun showNotification(title: String, message: String, id: Long) {
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

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("navigate_to", "details")
            putExtra("details_id", id.toString())
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(1, notification)
    }
}