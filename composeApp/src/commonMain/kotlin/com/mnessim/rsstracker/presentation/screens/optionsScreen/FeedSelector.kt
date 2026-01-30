package com.mnessim.rsstracker.presentation.screens.optionsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FeedSelector(
    modifier: Modifier = Modifier,
    feeds: List<String>,
    blockedFeeds: List<String> = emptyList(),
    toggleBlock: (String) -> Unit = {},
) {
    var showAlertDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            ),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                text = "Block results from specific sources",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            if (blockedFeeds.size > 1) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    text = if (blockedFeeds.size == 2) "${blockedFeeds.size - 1} feed blocked" else "${blockedFeeds.size - 1} feeds blocked",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                    )
                )
            }
            Button(
                onClick = {
                    showAlertDialog = true
                },
                content = {
                    Text(
                        text = "Unblock all",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 16.sp,
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = modifier.height(500.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = feeds,
                    key = { it -> it }
                ) {
                    val blocked = blockedFeeds.contains(it)

                    Surface(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(
                                onClick = { toggleBlock(it) }
                            ),
                        color = if (blocked) Color.Red else MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "$it ${if (blocked) "BLOCKED" else ""}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    if (showAlertDialog) {
        UnblockAlert(
            onConfirm = {
                showAlertDialog = false
                for (f in blockedFeeds) {
                    toggleBlock(f)
                }
            },
            onDismiss = {
                showAlertDialog = false
            }
        )
    }
}

@Composable
fun UnblockAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Confirm")
            }
        },
        title = { Text("Unblock all feeds?") }
    )
}