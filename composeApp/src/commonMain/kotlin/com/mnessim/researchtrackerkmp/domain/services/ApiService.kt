package com.mnessim.researchtrackerkmp.domain.services

import com.mnessim.researchtrackerkmp.ConfigFlags
import com.mnessim.researchtrackerkmp.domain.models.Article
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class ApiService(private val client: HttpClient) {
    val url = ConfigFlags.DEV_BACKEND_URL
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun search(term: String): List<Article> {
        val response = client.get("$url/search/$term")
        val bodyString = response.bodyAsText()
        return json.decodeFromString<List<Article>>(bodyString)
    }
}