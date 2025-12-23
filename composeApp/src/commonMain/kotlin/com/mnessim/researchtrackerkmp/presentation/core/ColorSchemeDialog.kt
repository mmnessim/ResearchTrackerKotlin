package com.mnessim.researchtrackerkmp.presentation.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ColorSchemeDialog(
    onDismiss: () -> Unit,
    onColorSchemeChange: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose Color Scheme") },
        text = { Text("Select an option") },
        confirmButton = {

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { onColorSchemeChange("light") }) {
                    Text("Light")
                } // TextButton
                TextButton(onClick = { onColorSchemeChange("dark") }) {
                    Text("Dark")
                } // TextButton
            } // Row
        }, // confirmButton =
        dismissButton = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { onColorSchemeChange("lightContrast") }) {
                    Text("Light Contrast")
                } // TextButton
                TextButton(onClick = { onColorSchemeChange("darkContrast") }) {
                    Text("Dark Contrast")
                } // TextButton
            } // Row
        } // dismissButton =
    ) // AlertDialog
} // ColorSchemeDialog