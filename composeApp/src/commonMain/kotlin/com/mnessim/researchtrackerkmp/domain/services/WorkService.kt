package com.mnessim.researchtrackerkmp.domain.services

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WorkService {
    fun scheduleWork(tag: String, periodic: Boolean, intervalMinutes: Long)
    fun cancelWork(tag: String)
    suspend fun performWork(): Boolean
}