package com.mnessim.researchtrackerkmp.domain.services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import java.util.concurrent.TimeUnit

actual class WorkService : KoinComponent {
    private val appContext: Context
        get() = GlobalContext.getOrNull()
            ?.getOrNull<Context>()
            ?: throw IllegalStateException(
                "Android Context not available in Koin. Call startKoin { androidContext(app) } before using WorkService."
            )
    private val workManager: WorkManager
        get() = WorkManager.getInstance(appContext)

    actual fun scheduleWork(tag: String, periodic: Boolean, intervalMinutes: Long) {
        if (periodic) {
            val request = PeriodicWorkRequestBuilder<WorkerDelegate>(
                intervalMinutes, TimeUnit.MINUTES
            )
                .setInitialDelay(1, TimeUnit.SECONDS).build()
            workManager.enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.REPLACE, request)
        } else {
            val request = OneTimeWorkRequestBuilder<WorkerDelegate>().build()
            workManager.enqueueUniqueWork(tag, ExistingWorkPolicy.REPLACE, request)
        }
    }

    actual fun cancelWork(tag: String) {

    }

    actual suspend fun performWork(): Boolean {
        TODO("Not yet implemented")
    }
    
}

class WorkerDelegate(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {


    override suspend fun doWork(): Result {
        val TAG = "WorkerDelegate"
        val koin = GlobalContext.getOrNull()
        if (koin == null) {
            Log.e(TAG, "Koin not started; retrying later")
            return Result.retry()
        }

        val client = koin.getOrNull<HttpClient>()
        if (client == null) {
            Log.e(TAG, "Client not found in Koin;")
            return Result.retry()
        }

        val apiService = ApiService(client)

        val termsRepo = koin.getOrNull<ITermsRepo>()
        if (termsRepo == null) {
            Log.e(TAG, "TermsRepo not found in Koin; retrying later")
            return Result.retry()
        }

        val manager = koin.getOrNull<NotificationManager>()

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
                if (t.lastArticleGuid != articles[0].guid && manager != null) {
                    manager.showNotification("${t.term} Results", "Tap to see new results", t.id)
                }
            }
        }

        return Result.success()
    }

}
