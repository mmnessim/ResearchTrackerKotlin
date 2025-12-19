package com.mnessim.researchtrackerkmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.mnessim.researchtrackerkmp.presentation.screens.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Research Tracker") }
            )
        }
    ) { innerPadding ->
        HomeScreen(modifier = Modifier.padding(innerPadding).fillMaxSize())
    }
//    MaterialTheme {
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Text("Research Tracker", style = TextStyle(fontWeight = FontWeight.Bold))
//            AddTermButton(onClick = { showDialog = true })
//            if (showDialog) {
//                AddTermAlert(
//                    onSubmit = { showDialog = false },
//                    onDismiss = { showDialog = false }
//                )
//            }
//        }
//    }
}

@Composable
fun AddTermButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tag: String = "AddTermButton"
) {
    IconButton(onClick = onClick, modifier = modifier.semantics { testTag = tag }) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add new term"
        )
    }
}

@Composable
fun AddTermAlert(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = onSubmit) {
                Text("Confirm")
            }
        },
        title = { Text("Alert Dialog") },
        text = {
            Column {
                Text("This is an alert dialog")
                TextField(state = TextFieldState())
            }
        }
    )
}