package com.mnessim.researchtrackerkmp.utils.notifications

// TODO: add time support, will need kotlinx.datetime
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NotificationManager() {
    fun showNotification(title: String, message: String, id: Long)
}

