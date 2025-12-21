package com.mnessim.researchtrackerkmp.utils.notifications

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType

actual class NotificationManager {
    actual fun showNotification(title: String, message: String) {
        if (!SystemTray.isSupported()) return

        val tray = SystemTray.getSystemTray()
        val image = Toolkit.getDefaultToolkit().createImage(ByteArray(0))
        val trayIcon = TrayIcon(image, "Notification").apply {
            isImageAutoSize = true
            toolTip = "Notification"
        }

        try {
            tray.add(trayIcon)
            trayIcon.displayMessage(title, message, MessageType.INFO)
            tray.remove(trayIcon)
        } catch (e: Exception) {
        }
    }
}