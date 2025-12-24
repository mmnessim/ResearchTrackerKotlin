package com.mnessim.researchtrackerkmp

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object NavigationEvents {
    private val _navigateToDetails = MutableSharedFlow<Long?>(replay = 1)
    val navigateToDetails: SharedFlow<Long?> = _navigateToDetails

    fun triggerNavigateToDetails(id: Long?) {
        _navigateToDetails.tryEmit(id)
    }
}