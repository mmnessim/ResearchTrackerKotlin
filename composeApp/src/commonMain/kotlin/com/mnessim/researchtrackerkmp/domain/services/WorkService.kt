package com.mnessim.researchtrackerkmp.domain.services

expect class WorkService {
    fun scheduleWork(tag: String, periodic: Boolean = false, intervalMinutes: Long = 15)
    fun cancelWork(tag: String)
    suspend fun performWork(): Boolean
}