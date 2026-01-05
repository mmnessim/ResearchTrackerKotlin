package com.mnessim.researchtrackerkmp.presentation.screens.navTilesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTilesScreen(
    modifier: Modifier = Modifier,
    onHome: () -> Unit,
    onOptions: () -> Unit,
    onAbout: () -> Unit
) {
    val showAlert = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            NavTile(title = "Terms", onClick = onHome, tileIcon = {
                Icon(
                    imageVector = Icons.Default.Newspaper,
                    contentDescription = "Tracked Terms",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            })
            NavTile(title = "Saved Articles", onClick = { showAlert.value = true }, tileIcon = {
                Icon(
                    imageVector = Icons.Default.Bookmarks,
                    contentDescription = "Bookmarked Articles",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            })
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            NavTile(title = "Options", onClick = onOptions, tileIcon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            })
            NavTile(
                title = "About",
                onClick = onAbout, tileIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                })
        }
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            title = { Text("Coming Soon") },
            text = { Text("This screen is under construction, but will be available in the future") },
            dismissButton = {
                TextButton(
                    onClick = { showAlert.value = false },
                    content = { Text("Dismiss") })
            },
            confirmButton = {
                TextButton(
                    onClick = { showAlert.value = false },
                    content = { Text("Okay") })
            },
        )
    }
}

@Composable
fun NavTile(
    title: String,
    tileIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = "Placeholder",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    },
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(160.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            tileIcon()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavTilesScreenPreview() {
    NavTilesScreen(onHome = {}, onOptions = {}, onAbout = {})
}