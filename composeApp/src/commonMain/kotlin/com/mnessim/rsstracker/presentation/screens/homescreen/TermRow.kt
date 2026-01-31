package com.mnessim.rsstracker.presentation.screens.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnessim.rsstracker.domain.models.Term

@Deprecated("Old UI implementation")
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
        if (term.hasNewArticle) {
            Icon(
                imageVector = Icons.Default.Circle, // or any small icon you prefer
                contentDescription = "New Article",
                tint = MaterialTheme.colorScheme.primaryFixedDim,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(12.dp)
            )
        }
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

@Composable
fun TermRowB(
    modifier: Modifier = Modifier,
    term: Term,
    onDelete: () -> Unit,
    onDeleteBlocked: () -> Unit,
    onToggleLock: () -> Unit,
    onNavigate: (Long) -> Unit,
    onNotificationButton: () -> Unit
) {
    val animatedElevation by animateFloatAsState(
        targetValue = if (term.hasNewArticle) 6f else 2f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .testTag("TermRow"),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = animatedElevation.dp,
        shadowElevation = animatedElevation.dp,
    ) {
        Column(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onNavigate(term.id) })
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = term.term,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (term.hasNewArticle) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (term.locked) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = term.hasNewArticle,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    contentDescription = null,
                                    modifier = Modifier.size(8.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "New articles",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        modifier = Modifier.testTag("ToggleLockButton"),
                        onClick = onToggleLock
                    ) {
                        Icon(
                            imageVector = if (term.locked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = if (term.locked) "Unlock" else "Lock",
                            tint = if (term.locked)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Notifications
                    IconButton(
                        modifier = Modifier.testTag("TermNotificationsButton"),
                        onClick = onNotificationButton
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Delete
                    IconButton(
                        modifier = Modifier.testTag("DeleteButton"),
                        onClick = {
                            if (!term.locked) onDelete() else onDeleteBlocked()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = if (term.locked)
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            else
                                MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Optional: Subtle divider at bottom for visual separation
            if (term.hasNewArticle) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            }
        }
    }
}