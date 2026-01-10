package com.mnessim.researchtrackerkmp.presentation.screens.savedArticlesScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.models.Article
import com.mnessim.researchtrackerkmp.domain.repositories.SavedArticlesRepo
import com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen.ArticleTile
import org.koin.compose.koinInject

@Composable
fun SavedArticlesScreen(modifier: Modifier = Modifier) {
    val savedArticlesRepo = koinInject<SavedArticlesRepo>()
    val articles = savedArticlesRepo.getAllArticlesFlow().collectAsState(initial = emptyList())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(articles.value, key = { it.guid ?: it.title }) { article: Article ->
            ArticleTile(
                article = article,
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
            )
        }
    }

}