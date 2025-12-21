package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag


@Composable
fun AddTermAlert(
    textFieldState: TextFieldState,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    tag: String,
) {
    AlertDialog(
        modifier = Modifier.testTag(tag),
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("DismissButton")
            ) {
                Text("Dismiss")
            } // TextButton
        }, // dismissButton
        confirmButton = {
            TextButton(
                onClick = onSubmit,
                modifier = Modifier.testTag("SubmitButton")
            ) {
                Text("Confirm")
            } // TextButton
        }, // confirmButton
        title = { Text("Alert Dialog") },
        text = {
            Column {
                Text("This is an alert dialog")
                TextField(
                    state = textFieldState,
                    modifier = Modifier.testTag("TermTextField")
                )
            } // Column
        } // text
    ) // AlertDialog
}