package com.mnessim.researchtrackerkmp.presentation.screens.optionsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import com.mnessim.researchtrackerkmp.domain.services.WorkService
import com.mnessim.researchtrackerkmp.presentation.core.ensureScheduled
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import kotlin.math.roundToInt

@Composable
fun OptionsWorkInterval(
    modifier: Modifier = Modifier
) {
    val workService = koinInject<WorkService>()
    val prefsRepo = koinInject<PreferencesRepo>()

    val options = listOf(15, 30, 45, 60, 120, 300, 480, 720)
    var intervalIndex by remember { mutableStateOf(0f) }
    var isSaved by remember { mutableStateOf(true) }
    var isSavedUI by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val savedMinutes = prefsRepo.getPrefByKey("workInterval")?.toIntOrNull()
        if (savedMinutes == null) {
            isSaved = false
            intervalIndex = 0f
        } else {
            isSaved = true
            val idx = options.indexOf(savedMinutes).takeIf { it >= 0 } ?: 0
            intervalIndex = idx.toFloat()
        }
    }

    fun labelFor(minutes: Int): String {
        return if (minutes % 60 == 0) {
            val hours = minutes / 60
            "${hours} ${if (hours == 1) "hour" else "hours"}"
        } else {
            "${minutes} min"
        }
    }

    val currentMinutes = options[intervalIndex.roundToInt()]

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Set refresh interval")
                        }
                        append(": ${labelFor(currentMinutes)}")
                    },
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (isSavedUI) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 2.dp,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Saved",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Row {
                Slider(
                    value = intervalIndex,
                    onValueChange = {
                        if (intervalIndex != it) {
                            isSavedUI = false
                        }
                        intervalIndex = it
                    },
                    onValueChangeFinished = {
                        // snap to nearest discrete position visually
                        intervalIndex = intervalIndex.roundToInt().toFloat()
                    },
                    valueRange = 0f..(options.lastIndex.toFloat()),
                    steps = options.size - 2,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.onSurface,
                        activeTrackColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        isSavedUI = true
                        val minutesToSave = options[intervalIndex.roundToInt()]
                        if (isSaved) {
                            prefsRepo.updatePref("workInterval", minutesToSave.toString())
                        } else {
                            prefsRepo.insertPref("workInterval", minutesToSave.toString())
                        }
                        ensureScheduled(workService, prefsRepo)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save")
                }
            }

            Row {
                Text(
                    text = "This value determines how frequently the app checks for updates and how frequently " +
                            "you could possibly receive notifications",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun OptionsWorkIntervalPreview() {
    OptionsWorkInterval()
}