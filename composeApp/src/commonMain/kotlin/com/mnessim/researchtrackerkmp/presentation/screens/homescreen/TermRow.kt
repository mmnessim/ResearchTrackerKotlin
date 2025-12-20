package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mnessim.researchtrackerkmp.domain.models.Term

@Composable
fun TermRow(
    modifier: Modifier = Modifier,
    term: Term,
    onDelete: () -> Unit,
    onToggleLock: () -> Unit
) {
    Row(
        modifier = modifier.testTag("TermRow"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(term.term)
        IconButton(
            modifier = Modifier.testTag("ToggleLockButton"),
            onClick = onToggleLock,
            content = {
                Icon(
                    imageVector = if (term.locked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = "Toggle Locked",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            })
        IconButton(
            modifier = Modifier.testTag("TermNotificationsButton"),
            onClick = {},
            content = {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Manage Notifications",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            })
        IconButton(
            modifier = Modifier.testTag("DeleteButton"),
            onClick = {
                if (!term.locked) onDelete()
            },
            content = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete term",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        )
    }
}