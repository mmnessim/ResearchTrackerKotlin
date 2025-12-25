package com.mnessim.researchtrackerkmp.domain.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual class HttpClientProvider {
    actual fun getClient(): HttpClient {
        return HttpClient(OkHttp)
    }
}