package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnessim.researchtrackerkmp.domain.models.Term

@Composable
fun TermRow(
    modifier: Modifier = Modifier,
    term: Term,
    onDelete: () -> Unit,
    onDeleteBlocked: () -> Unit,
    onToggleLock: () -> Unit,
    onNavigate: (Long) -> Unit,
    onNotificationButton: () -> Unit
) {
    Box(
        modifier = modifier.clickable(onClick = { onNavigate(term.id) })
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .testTag("TermRow")
                .fillMaxWidth(.9f),

            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                term.term,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp
                )
            ) // Text
            IconButton(
                modifier = Modifier.testTag("ToggleLockButton"),
                onClick = onToggleLock,
                content = {
                    Icon(
                        imageVector = if (term.locked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Toggle Locked",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ) // IconButton
            IconButton(
                modifier = Modifier.testTag("TermNotificationsButton"),
                onClick = onNotificationButton,
                content = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Manage Notifications",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ) // IconButton
            IconButton(
                modifier = Modifier.testTag("DeleteButton"),
                onClick = {
                    if (!term.locked) onDelete() else onDeleteBlocked()
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete term",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ) // IconButton
        } // Row
    } // Box
} // TermRow