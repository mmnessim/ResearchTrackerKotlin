package com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mnessim.researchtrackerkmp.domain.models.Article
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.domain.services.ApiService
import io.ktor.client.HttpClient
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
    val term = viewModel.term

    LaunchedEffect(key1 = id) {
        viewModel.fetch()
    }

//    val articles = listOf(placeholderArticle)

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (term != null) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = term.term,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                )
            } // Row
        } // if (term != null)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (a in articles.value) {
                ArticleTile(a)
            }
        }

        Button(onClick = onBack) {
            Text("Back")
        } // Button
    } // Column
}

@Composable
fun ArticleTile(article: Article) {
    val baseFontSize = 16

    Text(
        text = "${article.title} - ${article.creator}",
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = (baseFontSize + 8).sp
        )
    )
    Text(
        text = article.description,
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = (baseFontSize).sp
        )
    )
    for (c in article.categories) {
        Text(
            text = c,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = (baseFontSize).sp
            )
        )
    }
    if (article.mediaContentUrl != null) {
        AsyncImage(
            model = article.mediaContentUrl,
            contentDescription = article.mediaDescription,
            modifier = Modifier
                .fillMaxWidth(.9f)
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
    }
}