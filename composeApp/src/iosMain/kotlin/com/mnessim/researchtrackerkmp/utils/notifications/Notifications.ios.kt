package com.mnessim.researchtrackerkmp.utils.notifications

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter

actual class NotificationManager actual constructor() {
    actual fun showNotification(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
        }

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "local_notification",
            content = content,
            trigger = null
        )

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
    }
}