package com.mnessim.rsstracker.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.mnessim.rsstracker.domain.repositories.ITermsRepo
import com.mnessim.rsstracker.presentation.screens.detailsscreen.DetailsScreen
import com.mnessim.rsstracker.presentation.screens.homescreen.FakeTermsRepo
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.Test

class DetailsScreenTest {

    val mockEngine = MockEngine { request ->
        respond(
            content = "[" +
                    "{\n" +
                    "    \"rssSource\": \"New York Times\",\n" +
                    "    \"guid\": \"https://www.nytimes.com/2025/12/27/world/americas/venezuela-blockade-1903-us.html\",\n" +
                    "    \"title\": \"A Dancing Dictator and Bankers in Chains: The Other Venezuela Blockade\",\n" +
                    "    \"link\": \"https://www.nytimes.com/2025/12/27/world/americas/venezuela-blockade-1903-us.html\",\n" +
                    "    \"pubDate\": \"Sat, 27 Dec 2025 10:01:53 +0000\",\n" +
                    "    \"author\": \"Simon Romero\",\n" +
                    "    \"description\": \"A crisis more than a century ago involved U.S. aims to assert military supremacy, a hard-partying dictator and frictions among the great powers.\",\n" +
                    "    \"categories\": [\n" +
                    "      \"United States International Relations\",\n" +
                    "      \"International Relations\",\n" +
                    "      \"Oil (Petroleum) and Gasoline\",\n" +
                    "      \"Embargoes and Sanctions\",\n" +
                    "      \"Politics and Government\",\n" +
                    "      \"Castro\",\n" +
                    "      \"Cipriano\",\n" +
                    "      \"Roosevelt\",\n" +
                    "      \"Theodore\",\n" +
                    "      \"Maduro\",\n" +
                    "      \"Nicolas\",\n" +
                    "      \"Trump\",\n" +
                    "      \"Donald J\",\n" +
                    "      \"Venezuela\",\n" +
                    "      \"Germany\",\n" +
                    "      \"Great Britain\",\n" +
                    "      \"Italy\",\n" +
                    "      \"Latin America\"\n" +
                    "    ]\n" +
                    "  }" +
                    "]",
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
        )
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun detailsScreenTest() = runComposeUiTest {
        val testModule = module {
            single<ITermsRepo> { FakeTermsRepo() }
            single<HttpClient> { HttpClient(mockEngine) }
        }

        startKoin {
            modules(testModule)
        }

        try {
            setContent {
                DetailsScreen(
                    onBack = {},
                    id = 1L,
                    onError = {}
                )
            }
            onNodeWithTag("DetailsScreenOuterColumn").assertExists()
            onNodeWithTag("Term").assertExists()
            onNodeWithTag("DetailsScreenTermRow").assertExists()
            onNodeWithTag("ArticlesColumn").assertExists()
            onNodeWithTag("ArticleTile").assertExists()
            onNodeWithTag("BackButton").assertExists()
        } finally {
            stopKoin()
        }
    }
}