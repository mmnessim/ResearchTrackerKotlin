package com.mnessim.researchtrackerkmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mnessim.researchtrackerkmp.domain.data.DBFactory
import com.mnessim.researchtrackerkmp.domain.services.WorkService
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = application {
    val jvmPlatformModule = module {
        single<DBFactory> { DBFactory() }
        single<WorkService> { WorkService() }
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