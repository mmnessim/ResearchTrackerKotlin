package com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.domain.services.ApiService
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    id: Long
) {
    val repo = koinInject<ITermsRepo>()
    val client = koinInject<HttpClient>()
    val apiService = ApiService(client)
    val viewModel = remember(id) {
        DetailsScreenViewModel(
            apiService = apiService,
            id = id,
            termsRepo = repo
        )
    }
    val articles = viewModel.response.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val term = viewModel.term

    var showAmount by remember { mutableStateOf(10) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = id) {
        viewModel.fetch()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("DetailsScreenOuterColumn"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { viewModel.sort("source") }) {
            Text("ReSort")
        }
        Row(
            modifier = Modifier.fillMaxWidth(.9f)
                .testTag("DetailsScreenTermRow"),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.testTag("Term"),
                text = "${term.term} - ${articles.value.size} Results",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
            )
        }

        Text("GUID: ${term.lastArticleGuid ?: "None"}")

        if (!loading.value) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .fillMaxWidth()
                    .testTag("ArticlesColumn"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(articles.value.take(showAmount), key = { it.guid }) { a ->

                    ArticleTile(
                        modifier = Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        ).testTag("ArticleTile"),
                        article = a
                    )

                }
            }
        } else {
            Box(modifier = Modifier.weight(1f)) {}
        }

        Row {
            Button(onClick = {
                val old = showAmount
                showAmount += 10

                val newly = articles.value.drop(old).take(showAmount - old).map { it.guid }
                if (newly.isNotEmpty()) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(old)
                    }
                }
            }) {
                Text("Show more")
            }
            Button(onClick = onBack, modifier = Modifier.testTag("BackButton")) {
                Text("Back")
            }
        }

        if (loading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.testTag("DetailsScreenLoading"))
            }
        }
    }
}
