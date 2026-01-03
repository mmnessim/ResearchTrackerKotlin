package com.mnessim.researchtrackerkmp

import com.mnessim.Database
import com.mnessim.researchtrackerkmp.domain.data.DBFactory
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import com.mnessim.researchtrackerkmp.domain.repositories.TermsRepo
import com.mnessim.researchtrackerkmp.domain.services.ColorSchemeService
import com.mnessim.researchtrackerkmp.domain.services.HttpClientProvider
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {
    single<Database> {
        val factory = get<DBFactory>()
        Database(factory.createDriver())
    }
}

val termsRepoModule = module {
    single<ITermsRepo> {
        val database = get<Database>()
        TermsRepo(database)
    }
}

val prefsRepoModule = module {
    single<PreferencesRepo> {
        val database = get<Database>()
        PreferencesRepo(database)
    }
}

val notificationsModule = module {
    single<NotificationManager> {
        NotificationManager()
    }
}

val clientModule = module {
    single { HttpClientProvider().getClient() }
}

val colorModule = module {
    single<ColorSchemeService> {
        ColorSchemeService()
    }
}


val commonModules: List<Module> = listOf(
    databaseModule,
    termsRepoModule,
    prefsRepoModule,
    notificationsModule,
    clientModule,
    colorModule,
)

//fun initKoin(vararg platformModules: Module) {
//    val modulesList = listOf<Module>(databaseModule) + platformModules
//    startKoin {
//        modules(modulesList)
//    }
//}