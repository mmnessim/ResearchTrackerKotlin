package com.mnessim.researchtrackerkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.mnessim.researchtrackerkmp.domain.data.DBFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

fun MainViewController() = ComposeUIViewController {
    val iosPlatformModule = module {
        single<DBFactory> { DBFactory() }
    }
    startKoin {
        modules(commonModules + iosPlatformModule)
    }

    MainScope().launch {
        requestNotificationPermissions()
    }

    App()
}

fun requestNotificationPermissions() {
    val center = UNUserNotificationCenter.currentNotificationCenter()
    center.requestAuthorizationWithOptions(
        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
        completionHandler = { granted, error -> }
    )
}