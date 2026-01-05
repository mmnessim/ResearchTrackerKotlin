package com.mnessim.researchtrackerkmp.domain.services

import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
import io.ktor.client.HttpClient
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

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class WorkService : KoinComponent {
    private val termsRepo by inject<ITermsRepo>()
    private val client by inject<HttpClient>()
    private val apiService = ApiService(client)
    private val manager by inject<NotificationManager>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    actual fun scheduleWork(
        tag: String,
        periodic: Boolean,
        intervalMinutes: Long
    ) {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            identifier = "com.mnessim.researchtrackerkmp.fetch",
            usingQueue = null
        ) { task ->
            println("Refreshing GUIDs")
            scope.launch {
                performWork()
            }

            scheduleAppRefreshTask()
            (task as? BGAppRefreshTask)?.setTaskCompletedWithSuccess(true)
        }

    }

    actual fun cancelWork(tag: String) {
    }

    actual suspend fun performWork(): Boolean {
        return try {
            val terms = termsRepo.getAllTerms()
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
                    if (t.lastArticleGuid != articles[0].guid) {
                        manager.showNotification(
                            "${t.term} Results",
                            "Tap to see new results",
                            t.id
                        )
                    }
                }
            }
            true
        } catch (e: Exception) {
            println("Error updating GUIDs $e")
            false
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun scheduleAppRefreshTask() {
        val request =
            BGAppRefreshTaskRequest(identifier = "com.mnessim.researchtrackerkmp.processing")
        request.earliestBeginDate =
            NSDate().dateByAddingTimeInterval((15 * 60).toDouble()) // 15 minutes
        BGTaskScheduler.sharedScheduler.submitTaskRequest(request, null)
    }

}