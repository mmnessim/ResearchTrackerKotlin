package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.repositories.TermsRepo
import org.koin.compose.koinInject


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Long) -> Unit,
) {
    val repo = koinInject<TermsRepo>()
    val viewmodel = remember { HomeScreenViewModel(repo) }
    val terms by viewmodel.terms.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            for ((index, term) in terms.withIndex()) {
                TermRow(
                    term = term,
                    onDelete = { viewmodel.removeTerm(term.id) },
                    onToggleLock = { viewmodel.toggleLocked(term) },
                    onNavigate = { id -> onNavigate(id) },
                    modifier = Modifier
                        .then(
                            if (index < terms.lastIndex) Modifier.padding(bottom = 8.dp) else Modifier
                        )
                )
            }
        } // Column

        AddTermButton(
            onClick = { showAlertDialog = true }
        )

        if (showAlertDialog) {
            AddTermAlert(
                textFieldState = viewmodel.controller,
                onSubmit = {
                    val input = viewmodel.controller.text.toString()
                    if (input.isNotEmpty()) {
                        viewmodel.addTerm(false)
                        viewmodel.controller.clearText()
                    }
                    showAlertDialog = false
                },
                onDismiss = { showAlertDialog = false },
                tag = "TermAlertDialog"
            ) // AddTermAlert
        } // if (showAlertDialog)
    }
}