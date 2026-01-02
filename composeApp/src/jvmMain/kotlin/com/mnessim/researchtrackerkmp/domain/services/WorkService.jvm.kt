package com.mnessim.researchtrackerkmp.domain.services

import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.max

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
actual class WorkService {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val scheduler = Executors.newScheduledThreadPool(1)
    private val tasks = ConcurrentHashMap<String, ScheduledFuture<*>>()

    actual fun scheduleWork(
        tag: String,
        periodic: Boolean,
        intervalMinutes: Long
    ) {
        cancelWork(tag)

        val runnable = Runnable {
            scope.launch {
                try {
                    performWork()
                } catch (t: Throwable) {
                    println("WorkService: performWork failed for tag=$tag: ${t.message}")
                }
            }
        }

        if (periodic) {
            val period = max(1L, intervalMinutes)
            val future = scheduler.scheduleAtFixedRate(runnable, 1, period, TimeUnit.MINUTES)
            tasks[tag] = future
        } else {
            val future = scheduler.schedule(runnable, 1, TimeUnit.SECONDS)
            tasks[tag] = future
        }
    }

    actual fun cancelWork(tag: String) {
        val future = tasks.remove(tag)
        future?.cancel(true)
    }

    actual suspend fun performWork(): Boolean {
        val koin = GlobalContext.getOrNull() ?: return false
        val client = koin.getOrNull<HttpClient>() ?: return false
        val termsRepo = koin.getOrNull<ITermsRepo>() ?: return false
        val manager = koin.getOrNull<NotificationManager>() ?: return false

        val apiService = ApiService(client)

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
            println("Error: $e")
            false
        }

    }
}