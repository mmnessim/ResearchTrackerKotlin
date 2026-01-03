package com.mnessim.researchtrackerkmp.presentation.screens.optionsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.services.ColorSchemeService
import com.mnessim.researchtrackerkmp.presentation.core.ColorSchemeDialog
import com.mnessim.researchtrackerkmp.presentation.theme.darkScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastDarkColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastLightColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import org.koin.compose.koinInject

@Composable
fun OptionsTheme(
    modifier: Modifier = Modifier
) {
    val colorService = koinInject<ColorSchemeService>()
    val coloScheme by colorService.scheme.collectAsState()
    var colorSchemeText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(coloScheme) {
        when (coloScheme) {
            lightScheme -> colorSchemeText = "Light"
            darkScheme -> colorSchemeText = "Dark"
            highContrastLightColorScheme -> colorSchemeText = "High Contrast Light"
            highContrastDarkColorScheme -> colorSchemeText = "High Contrast Dark"
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Set color scheme. Current: $colorSchemeText",
                style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
            )
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Change color scheme",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

    if (showDialog) {
        ColorSchemeDialog(
            activeScheme = coloScheme,
            onDismiss = { showDialog = false },
            onColorSchemeChange = { it -> colorService.setScheme(it) }
        )
    }
}