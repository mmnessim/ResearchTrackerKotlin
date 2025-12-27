package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import org.koin.compose.koinInject


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Long) -> Unit,
    onNotificationButton: (Term) -> Unit,
) {
    val repo = koinInject<ITermsRepo>()
    val viewmodel = remember { HomeScreenViewModel(repo) }
    val terms by viewmodel.terms.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for ((index, term) in terms.withIndex()) {
                TermRow(
                    term = term,
                    onDelete = { viewmodel.removeTerm(term.id) },
                    onToggleLock = { viewmodel.toggleLocked(term) },
                    onNavigate = { id -> onNavigate(id) },
                    onNotificationButton = { onNotificationButton(term) },
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