package com.mnessim.researchtrackerkmp.utils.notifications

// TODO: add time support, will need kotlinx.datetime
expect class NotificationManager() {
    fun showNotification(title: String, message: String)
}

