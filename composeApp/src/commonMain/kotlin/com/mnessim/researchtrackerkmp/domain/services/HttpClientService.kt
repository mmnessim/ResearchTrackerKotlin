package com.mnessim.researchtrackerkmp.domain.services

import io.ktor.client.HttpClient

expect class HttpClientProvider() {
    fun getClient(): HttpClient
}