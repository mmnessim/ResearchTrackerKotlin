package com.mnessim.rsstracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mnessim.rsstracker.domain.data.DBFactory
import com.mnessim.rsstracker.domain.services.IWorkService
import com.mnessim.rsstracker.domain.services.WorkService
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = application {
    val jvmPlatformModule = module {
        single<DBFactory> { DBFactory() }
        single<IWorkService> { WorkService() }
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "ResearchTrackerKMP",
    ) {
        startKoin {
            modules(commonModules + jvmPlatformModule)
        }
        App()
    }
}