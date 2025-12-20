package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mnessim.researchtrackerkmp.domain.repositories.TermsRepo
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import org.koin.compose.koinInject


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val repo = koinInject<TermsRepo>()
    val viewmodel = remember { HomeScreenViewModel(repo) }
    val terms by viewmodel.terms.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }
    MaterialTheme(
        colorScheme = lightScheme
    ) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Research Tracker",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier.testTag("Title")
                )
                for (term in terms) {
                    TermRow(
                        term = term,
                        onDelete = { viewmodel.removeTerm(term.id) },
                        onToggleLock = { viewmodel.toggleLocked(term) }
                    )
                }
            }

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
                )
            }
        }
    }
}