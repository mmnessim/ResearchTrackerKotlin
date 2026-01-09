package com.mnessim.researchtrackerkmp.presentation.screens.aboutScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.ConfigFlags
import com.mnessim.researchtrackerkmp.domain.models.Stats
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
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
    val prefsRepo = koinInject<PreferencesRepo>()
    var rustUrl by remember { mutableStateOf("") }

    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        stats = apiService.getStats()
        rustUrl = prefsRepo.getPrefByKey("rustUrl") ?: ""
    }

    Column(
        modifier = modifier
            .padding(12.dp),
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
                text = "Current app version: ${ConfigFlags.AppVersion}",
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
        Button(
            onClick = {
                when (prefsRepo.getPrefByKey("rustUrl")) {
                    null -> {
                        prefsRepo.insertPref("rustUrl", "true")
                        rustUrl = "true"
                    }

                    "true" -> {
                        prefsRepo.updatePref("rustUrl", "false")
                        rustUrl = "false"
                    }

                    else -> {
                        prefsRepo.updatePref("rustUrl", "true")
                        rustUrl = "true"
                    }
                }
            }
        ) {
            Text("Experimental backend: $rustUrl")
        }
        Row(
            modifier = Modifier
                .clickable(
                    onClick = {
                        uriHandler.openUri("https://github.com/mmnessim/ResearchTrackerKotlin")
                    }
                )
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = "View the code on Github",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    textDecoration = TextDecoration.Underline,
                )
            )
        }
        // Maybe add a feedback form or something
    }
}