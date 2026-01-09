package com.mnessim.researchtrackerkmp.domain.services

import com.mnessim.researchtrackerkmp.ConfigFlags
import com.mnessim.researchtrackerkmp.domain.models.Article
import com.mnessim.researchtrackerkmp.domain.models.Stats
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApiService(private val client: HttpClient) : KoinComponent {
    val prefsRepo by inject<PreferencesRepo>()
    val rustUrl = prefsRepo.getPrefByKey("rustUrl") ?: "false"

    val url = if (rustUrl == "true") ConfigFlags.RUST_BACKEND_URL else ConfigFlags.DEV_BACKEND_URL
    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun search(term: String): List<Article> {
        return try {
            val response = client.get("$url/search/$term")
            val bodyString = response.bodyAsText()
            json.decodeFromString<List<Article>>(bodyString)
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }

    suspend fun checkHealth(): HttpStatusCode {
        try {
            val response = client.get("$url/health")
            return response.status
        } catch (e: Exception) {
            println("Error checking health: $e")
            return HttpStatusCode.InternalServerError
        }
    }

    suspend fun getStats(): Stats? {
        return try {
            val endpoint = if (rustUrl == "true") "$url/stats" else "$url/all"
            val response = client.get(endpoint)
            val bodyString = response.bodyAsText()
            json.decodeFromString<Stats>(bodyString)
        } catch (e: Exception) {
            println("Error getting stats $e")
            null
        }
    }
}