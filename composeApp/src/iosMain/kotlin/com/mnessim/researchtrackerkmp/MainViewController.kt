package com.mnessim.researchtrackerkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.mnessim.researchtrackerkmp.domain.data.DBFactory
import com.mnessim.researchtrackerkmp.domain.services.WorkService
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.Foundation.NSNumber
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

private var koinApp: KoinApplication? = null

fun initKoinIfNeeded(): KoinApplication {
    if (koinApp == null) {
        val iosPlatformModule = module {
            single<DBFactory> { DBFactory() }
            single<WorkService> { WorkService() }
        }
        koinApp = startKoin {
            modules(commonModules + iosPlatformModule)
        }
    }
    return koinApp!!
}

fun MainViewController(detailsId: NSNumber? = null) = ComposeUIViewController {
    val koinApp = initKoinIfNeeded()

    MainScope().launch {
        requestNotificationPermissions()
        val workService = koinApp.koin.get<WorkService>()
        workService.checkForUpdatesOnAppLaunch()
    }

    App(startDestination = if (detailsId == null) NavTilesRoute else DetailsRoute(id = detailsId.longValue))
}

fun requestNotificationPermissions() {
    val center = UNUserNotificationCenter.currentNotificationCenter()
    center.requestAuthorizationWithOptions(
        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
        completionHandler = { granted, error -> }
    )
}

