package com.mnessim.rsstracker.presentation.screens.aboutScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mnessim.rsstracker.Constants
import com.mnessim.rsstracker.domain.models.Stats
import com.mnessim.rsstracker.domain.services.ApiService
import com.mnessim.rsstracker.isIos
import io.ktor.client.HttpClient
import org.koin.compose.koinInject

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val client = koinInject<HttpClient>()
    val apiService = ApiService(client)
    var stats by remember { mutableStateOf<Stats?>(null) }
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        stats = apiService.getStats()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                AboutTile(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                    title = "About",
                    description = "RSSTracker lets you track news on topics you care about",
                    extraText = "No accounts, no data sharing, just pure privacy. Instantly search " +
                            "and follow terms, with your preferences stored only on your device. " +
                            "Stay informed with fresh articles from trusted RSS feeds, all in one " +
                            "simple, secure app."
                )
            }
            item {
                if (isIos) {
                    AboutTile(
                        title = "App Version",
                        description = "${Constants.APP_VERSION}/iOS"
                    )
                } else {
                    AboutTile(
                        title = "App Version",
                        description = "${Constants.APP_VERSION}/Android"
                    )
                }
            }

            item {
                AboutTile(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                    title = "Developer Information",
                    description = "Developed by Mounir Nessim",
                    extraText = "Email mnessimdev@gmail.com to provide feedback"
                )
            }

            item {
                AboutTile(
                    title = "News Sources",
                    description = "Articles are fetched from public RSS feeds every 15 minutes and stored for 30 days.",
                    extraText = stats?.let { "Current count: ${it.numArticles} articles from ${it.numSources} RSS feeds" }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                uriHandler.openUri("https://github.com/mmnessim/ResearchTrackerKotlin")
                            }
                        )
                ) {
                    AboutTile(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ),
//                            .clickable(
//                                onClick = {
//                                    uriHandler.openUri("https://github.com/mmnessim/ResearchTrackerKotlin")
//                                }
//                            ),
                        title = "Source Code",
                        description = "View on Github"
                    )
                }
            }
            item {
                FeedsList(
                    modifier = Modifier.fillMaxWidth()
                        .height(600.dp)
                )
            }
        }
        // Maybe add a feedback form or something
    }
}