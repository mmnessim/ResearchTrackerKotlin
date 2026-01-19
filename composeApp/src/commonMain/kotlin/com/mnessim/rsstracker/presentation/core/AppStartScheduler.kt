package com.mnessim.rsstracker.presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mnessim.rsstracker.domain.repositories.PreferencesRepo
import com.mnessim.rsstracker.domain.services.IWorkService
import org.koin.compose.koinInject

@Composable
fun AppStartScheduler() {
    val scheduled = remember { mutableStateOf(false) }
    val workService = koinInject<IWorkService>()
    val prefsRepo = koinInject<PreferencesRepo>()

    LaunchedEffect(Unit) {
        println("Scheduler called")
        if (!scheduled.value) {
            ensureScheduled(workService, prefsRepo)
            scheduled.value = true
        }
    }
}

fun ensureScheduled(workService: IWorkService, prefsRepo: PreferencesRepo) {
    val workInterval = prefsRepo.getPrefByKey("workInterval")?.toLongOrNull() ?: 15L
    println("Scheduling background work (ensureScheduled) with interval $workInterval")
    workService.scheduleWork("test", true, workInterval) // TODO change back to workInterval
}