package com.mnessim.rsstracker.presentation.screens.savedArticlesScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnessim.rsstracker.domain.models.Article
import com.mnessim.rsstracker.domain.repositories.SavedArticlesRepo
import com.mnessim.rsstracker.presentation.screens.detailsscreen.ArticleTile
import org.koin.compose.koinInject

@Composable
fun SavedArticlesScreen(modifier: Modifier = Modifier) {
    val savedArticlesRepo = koinInject<SavedArticlesRepo>()
    val articles = savedArticlesRepo.getAllArticlesFlow().collectAsState(initial = emptyList())

    if (articles.value.isEmpty()) {
        Row(
            modifier = modifier.fillMaxSize().padding(12.dp)
        ) {
            Text(
                text = "No articles saved",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(articles.value, key = { it.guid ?: it.title }) { article: Article ->
                ArticleTile(
                    article = article,
                )
            }
        }
    }


}