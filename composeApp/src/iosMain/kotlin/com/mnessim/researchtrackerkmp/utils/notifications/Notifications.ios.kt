package com.mnessim.researchtrackerkmp.utils.notifications

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSDictionary
import platform.Foundation.NSMutableDictionary
import platform.Foundation.NSString
import platform.Foundation.create
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

@OptIn(BetaInteropApi::class)
fun Map<String, Any?>.toNSDictionary(): NSDictionary {
    val dict = NSMutableDictionary()
    for ((key, value) in this) {
        dict.setObject(value as Any, forKey = NSString.create(string = key))
    }
    return dict
}

actual class NotificationManager actual constructor() {
    actual fun showNotification(title: String, message: String) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setUserInfo(
                mapOf(
                    "navigate_to" to "details",
                    "details_id" to 1L
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

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request, null)
    }
}