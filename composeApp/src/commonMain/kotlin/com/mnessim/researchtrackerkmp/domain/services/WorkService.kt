package com.mnessim.researchtrackerkmp.domain.services

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WorkService {
    fun scheduleWork(tag: String, periodic: Boolean = false, intervalMinutes: Long = 15)
    fun cancelWork(tag: String)
    suspend fun performWork(): Boolean
}