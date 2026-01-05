package com.mnessim.researchtrackerkmp.presentation.screens.aboutScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.models.Stats
import com.mnessim.researchtrackerkmp.domain.services.ApiService
import io.ktor.client.HttpClient
import org.koin.compose.koinInject

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val client = koinInject<HttpClient>()
    val apiService = ApiService(client)
    var stats by remember { mutableStateOf<Stats?>(null) }
    LaunchedEffect(Unit) {
        stats = apiService.getStats()
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row {
            Text(
                text = "Developed by Mounir Nessim",
                style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            )
        }
        Row {
            Text(
                text = "Current app version: 1.0.6",
                style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
            )
        }
        Row {
            SelectionContainer {
                Text(
                    text = "To give feedback or suggest additional RSS sources, email mnessimdev@gmail.com",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        stats?.let {
            Text(
                text = "${it.numArticles} articles from ${it.numSources} RSS feeds",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
        Text(
            text = "Articles are fetched from feeds every 15 minutes and stored for 30 days",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
}