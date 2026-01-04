package com.mnessim.researchtrackerkmp.presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import com.mnessim.researchtrackerkmp.domain.services.WorkService
import org.koin.compose.koinInject

@Composable
fun AppStartScheduler() {
    val scheduled = remember { mutableStateOf(false) }
    val workService = koinInject<WorkService>()
    val prefsRepo = koinInject<PreferencesRepo>()

    LaunchedEffect(Unit) {
        println("Scheduler called")
        if (!scheduled.value) {
            ensureScheduled(workService, prefsRepo)
            scheduled.value = true
        }
    }
}

fun ensureScheduled(workService: WorkService, prefsRepo: PreferencesRepo) {
    val workInterval = prefsRepo.getPrefByKey("workInterval")?.toLongOrNull() ?: 15L
    println("Scheduling background work (ensureScheduled) with interval $workInterval")
    workService.scheduleWork("test", true, workInterval)
}