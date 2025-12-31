package com.mnessim.researchtrackerkmp.presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mnessim.researchtrackerkmp.domain.services.WorkService
import org.koin.compose.koinInject

@Composable
fun AppStartScheduler() {
    val scheduled = remember { mutableStateOf(false) }
    val workService = koinInject<WorkService>()

    LaunchedEffect(Unit) {
        println("Scheduler called")
        if (!scheduled.value) {
            println("Scheduling background work")
            workService.scheduleWork("test", true, 15)
            scheduled.value = true
        }
    }
}