package com.mnessim.rsstracker.domain.services

import com.mnessim.rsstracker.domain.models.Term
import com.mnessim.rsstracker.domain.repositories.ITermsRepo
import com.mnessim.rsstracker.domain.repositories.PreferencesRepo
import com.mnessim.rsstracker.utils.notifications.NotificationManager
import io.ktor.client.HttpClient
import kotlinx.atomicfu.atomic
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.BackgroundTasks.BGAppRefreshTask
import platform.BackgroundTasks.BGAppRefreshTaskRequest
import platform.BackgroundTasks.BGTaskScheduler
import platform.Foundation.NSDate
import platform.Foundation.dateByAddingTimeInterval
import platform.darwin.dispatch_get_main_queue

/**
 * WorkService has a very different iOS implementation compared to Android
 */
@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class WorkService : KoinComponent, IWorkService {
    private val termsRepo by inject<ITermsRepo>()
    private val prefsRepo by inject<PreferencesRepo>()
    private val client by inject<HttpClient>()
    private val apiService = ApiService(client)
    private val manager by inject<NotificationManager>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        private val bgTaskRegistered = atomic(0)
        private const val TASK_ID = "com.mnessim.rsstracker.fetch"
    }

    /**
     * scheduleWork both requests a background task with scheduleAppRefreshTask() and sets a reminder
     * notification based on workInterval in prefs with schedulePeriodicReminders()
     *
     * Background tasks are unreliable on iOS, so period reminders are necessary
     */
    actual override fun scheduleWork(
        tag: String,
        periodic: Boolean,
        intervalMinutes: Long
    ) {
        if (bgTaskRegistered.compareAndSet(expect = 0, update = 1)) {
            BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
                identifier = "com.mnessim.rsstracker.fetch",
                usingQueue = dispatch_get_main_queue()
            ) { task ->
                println("Refreshing GUIDs")
                scope.launch {
                    performWork()
                }

                scheduleAppRefreshTask(intervalMinutes)
                (task as? BGAppRefreshTask)?.setTaskCompletedWithSuccess(true)
            }
        }
        scheduleAppRefreshTask(intervalMinutes)
        schedulePeriodicReminders(getWorkInterval())

    }

    actual override fun cancelWork(tag: String) {
    }

    /**
     * performWork() cycles through all terms and checks for new results. It schedules notifications if new results
     *
     * It is not certain to be called by BGTaskScheduler on iOS
     */
    actual override suspend fun performWork(): Boolean {
        return try {
            val terms = termsRepo.getAllTerms()
            var hasNewResults = false
            for (t in terms) {
                println("Updating GUID for ${t.term}")
                val articles = apiService.search(t.term)
                if (articles.isNotEmpty() && t.lastArticleGuid != articles[0].guid) {
                    termsRepo.updateTerm(
                        Term(
                            id = t.id,
                            term = t.term,
                            locked = t.locked,
                            lastArticleGuid = articles[0].guid,
                            hasNewArticle = true
                        )
                    )
                    manager.showNotification(
                        "New results for ${t.term.replaceFirstChar { it.uppercase() }}",
                        "Tap to see new results",
                        t.id
                    )
                    hasNewResults = true
                }
            }
            val refreshInterval = getWorkInterval()
            println("Reminding again in $refreshInterval minutes")
            schedulePeriodicReminders(refreshInterval)
            hasNewResults
        } catch (e: Exception) {
            println("Error updating GUIDs $e")
            false
        }
    }

    /**
     * Sends a generic reminder to prompt users to open app
     */
    private fun schedulePeriodicReminders(intervalMinutes: Long) {
        manager.scheduleNotification(
            id = 0,
            title = "Research Tracker",
            message = "Tap to check for new updates",
            interval = intervalMinutes
        )
    }

    /**
     * refreshWithoutNotifications() replicates performWork() to update hasNewArticle flag without
     * scheduling notifications. It is called when users visit HomeScreen to update
     */
    actual override suspend fun refreshWithoutNotification(): Boolean {
        return try {
            val terms = termsRepo.getAllTerms()
            for (t in terms) {
                println("Updating GUID for ${t.term}")
                val articles = apiService.search(t.term)
                if (articles.isNotEmpty() && t.lastArticleGuid != articles[0].guid) {
                    println("New articles for ${t.term}")
                    termsRepo.updateTerm(
                        Term(
                            id = t.id,
                            term = t.term,
                            locked = t.locked,
                            lastArticleGuid = articles[0].guid,
                            hasNewArticle = true
                        )
                    )
                }
            }
            true
        } catch (e: Exception) {
            println("Error updating GUIDs $e")
            false
        }
    }

    // TODO probably remove old implementation altogether
    @OptIn(ExperimentalForeignApi::class)
    private fun scheduleAppRefreshTask(intervalMinutes: Long) {
        val request =
            BGAppRefreshTaskRequest(identifier = TASK_ID)
        request.earliestBeginDate =
            NSDate().dateByAddingTimeInterval((intervalMinutes * 60).toDouble())
        BGTaskScheduler.sharedScheduler.submitTaskRequest(request, null)
    }

    fun checkForUpdatesOnAppLaunch() {
        println("Checking for updates")
        scope.launch {
            performWork()
        }
    }

    private fun getWorkInterval(): Long {
        return prefsRepo.getPrefByKey("workInterval")?.toLongOrNull() ?: 60L
    }
}