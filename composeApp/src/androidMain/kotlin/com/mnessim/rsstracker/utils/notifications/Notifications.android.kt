package com.mnessim.rsstracker.utils.notifications

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import android.app.NotificationManager as AndroidNotificationManager

actual class NotificationManager actual constructor() {
    private lateinit var context: Context

    companion object {
        private const val CHANNEL_ID = "research_tracker_channel"
        private const val CHANNEL_NAME = "ResearchTracker"
        private const val CHANNEL_DESC = "ResearchTracker notifications"
    }

    fun init(context: Context) {
        this.context = context
    }

    actual fun showNotification(title: String, message: String, id: Long) {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                AndroidNotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
            }
            manager.createNotificationChannel(channel)
        }

        // Use an implicit deep-link intent so this KMP module doesn't need to reference MainActivity
        val deepLinkUri = "researchtracker://details/$id".toUri()
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            putExtra("navigate_to", "details")
            putExtra("details_id", id.toString())
            action = "com.mnessim.rsstracker.ACTION_SHOW_DETAILS_$id"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val requestCode = ((id xor (id ushr 32)).toInt())
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager.notify(id.toInt(), notification)
    }
}