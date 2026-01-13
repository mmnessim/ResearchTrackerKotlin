package com.mnessim.researchtrackerkmp.domain.services

import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
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

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class WorkService : KoinComponent {
    private val termsRepo by inject<ITermsRepo>()
    private val client by inject<HttpClient>()
    private val apiService = ApiService(client)
    private val manager by inject<NotificationManager>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    companion object {
        private val bgTaskRegistered = atomic(0)
        private const val TASK_ID = "com.mnessim.researchtrackerkmp.fetch"
    }

    actual fun scheduleWork(
        tag: String,
        periodic: Boolean,
        intervalMinutes: Long
    ) {
        if (bgTaskRegistered.compareAndSet(expect = 0, update = 1)) {
            BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
                identifier = "com.mnessim.researchtrackerkmp.fetch",
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
        schedulePeriodicReminders(1L)

    }

    actual fun cancelWork(tag: String) {
    }

    actual suspend fun performWork(): Boolean {
        return try {
            val terms = termsRepo.getAllTerms()
            var hasNewResults = false
            for (t in terms) {
                println("Updating GUID for ${t.term}")
                val articles = apiService.search(t.term)
                if (articles.isNotEmpty()) {
                    termsRepo.updateTerm(
                        Term(
                            id = t.id,
                            term = t.term,
                            locked = t.locked,
                            lastArticleGuid = articles[0].guid
                        )
                    )
                    hasNewResults = true
                    if (t.lastArticleGuid != articles[0].guid) {
                        manager.showNotification(
                            "New results for ${t.term.replaceFirstChar { it.uppercase() }}",
                            "Tap to see new results",
                            t.id
                        )
                    }
                }
            }
            // TODO: pull from preferences instead of being 4 hours
            schedulePeriodicReminders(4L * 60L)
            hasNewResults
        } catch (e: Exception) {
            println("Error updating GUIDs $e")
            false
        }
    }

    private fun schedulePeriodicReminders(intervalMinutes: Long) {
        // Schedule a reminder notification that prompts user to open app
        manager.scheduleNotification(
            id = 0,
            title = "Research Tracker",
            message = "Tap to check for new research updates",
            interval = intervalMinutes
        )
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
}