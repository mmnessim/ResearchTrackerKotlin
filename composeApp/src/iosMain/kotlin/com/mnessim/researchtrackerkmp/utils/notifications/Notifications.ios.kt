package com.mnessim.researchtrackerkmp.utils.notifications

import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
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

        UNUserNotificationCenter.currentNotificationCenter()
            .addNotificationRequest(request) { error ->
                if (error != null) {
                    // Handle error
                    println("Notification scheduling failed: $error")
                } else {
                    // Success
                    println("Notification scheduled successfully")
                }
            }
    }

    fun scheduleNotification(title: String, message: String, id: Long, interval: Long) {
        // TODO: rework content
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setUserInfo(
                mapOf(
                    "navigate_to" to "home",
                    "details_id" to -1L // set to -1 to route to HomeRoute
                )
            )
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
            timeInterval = interval * 60.0,
            repeats = true
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = "local_notification",
            content = content,
            trigger = trigger
        )

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)

    }
}