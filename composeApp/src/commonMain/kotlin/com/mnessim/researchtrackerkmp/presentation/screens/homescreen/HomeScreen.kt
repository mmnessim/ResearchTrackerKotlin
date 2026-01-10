package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (Long) -> Unit,
    onNavigateToTiles: () -> Unit,
    onNotificationButton: (Term) -> Unit,
) {
    val repo = koinInject<ITermsRepo>()
    val viewmodel = remember { HomeScreenViewModel(repo) }
    val terms by viewmodel.terms.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(terms) { term ->
                    TermRow(
                        term = term,
                        onDelete = { viewmodel.removeTerm(term.id) },
                        onDeleteBlocked = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Cannot delete locked term")
                            }
                        },
                        onToggleLock = { viewmodel.toggleLocked(term) },
                        onNavigate = { id -> onNavigate(id) },
                        onNotificationButton = { onNotificationButton(term) },
                    )
                }
            } // LazyColumn

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
}