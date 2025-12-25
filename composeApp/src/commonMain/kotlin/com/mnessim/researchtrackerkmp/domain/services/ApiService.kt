package com.mnessim.researchtrackerkmp.domain.services

import com.mnessim.researchtrackerkmp.ConfigFlags
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService(private val client: HttpClient) {
    val url = ConfigFlags.DEV_BACKEND_URL

    suspend fun search(term: String): String {
        val response = client.get(urlString = "$url/search/$term")
        println("Response code: ${response.status}")
        return response.body<String>()
    }
}