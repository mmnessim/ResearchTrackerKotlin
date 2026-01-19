package com.mnessim.rsstracker.domain.services

interface IWorkService {
    fun scheduleWork(tag: String, periodic: Boolean, intervalMinutes: Long)
    fun cancelWork(tag: String)
    suspend fun performWork(): Boolean
    suspend fun refreshWithoutNotification(): Boolean
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WorkService : IWorkService {
    override fun scheduleWork(tag: String, periodic: Boolean, intervalMinutes: Long)
    override fun cancelWork(tag: String)
    override suspend fun performWork(): Boolean
    override suspend fun refreshWithoutNotification(): Boolean
}