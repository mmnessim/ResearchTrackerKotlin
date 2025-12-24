package com.mnessim.researchtrackerkmp.utils.notifications

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

actual class NotificationManager actual constructor() {
    actual fun showNotification(title: String, message: String, id: Long) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setUserInfo(
                mapOf(
                    "navigate_to" to "details",
                    "details_id" to id
                )
            )
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
            timeInterval = 2.0,
            repeats = false
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "local_notification",
            content = content,
            trigger = trigger
        )

//        val center = UNUserNotificationCenter.currentNotificationCenter()

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
    }
}