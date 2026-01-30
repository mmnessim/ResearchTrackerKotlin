package com.mnessim.rsstracker.presentation.screens.optionsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mnessim.rsstracker.domain.repositories.PreferencesRepo
import com.mnessim.rsstracker.domain.services.ApiService
import io.ktor.client.HttpClient
import org.koin.compose.koinInject

@Composable
fun OptionsScreen(
    modifier: Modifier = Modifier
) {
    val prefsRepo = koinInject<PreferencesRepo>()
    val client = koinInject<HttpClient>()

    val vm = remember { OptionsScreenViewmodel(prefsRepo, apiService = ApiService(client)) }
    val feeds by vm.feeds.collectAsState(initial = emptyList())

    val blockedFeeds by vm.blocked.collectAsState(initial = emptyList())

    Column {
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OptionsTheme()
            }
            item {
                OptionsWorkInterval()
            }
            item {
                FeedSelector(
//                    modifier = Modifier.height(600.dp),
                    feeds = feeds,
                    blockedFeeds = blockedFeeds,
                    toggleBlock = { it -> vm.togglePref(it) }
                )
            }
        }
    }
}