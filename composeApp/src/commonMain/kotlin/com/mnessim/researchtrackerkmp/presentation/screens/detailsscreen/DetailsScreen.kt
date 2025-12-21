package com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mnessim.researchtrackerkmp.domain.repositories.TermsRepo
import org.koin.compose.koinInject

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    id: Long
) {
    val repo = koinInject<TermsRepo>()
    val term = repo.getTermById(id)
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (term != null) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(term.term)
                Text(if (term.locked) "Locked" else "Unlocked")
                Text(term.id.toString())
            } // Row
        } // if (term != null)
        Button(onClick = onBack) {
            Text("Back")
        } // Button
    } // Column
}
