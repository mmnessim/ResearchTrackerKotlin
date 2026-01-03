package com.mnessim.researchtrackerkmp.presentation.screens.optionsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import org.koin.compose.koinInject

@Composable
fun OptionsScreen(
    modifier: Modifier = Modifier
) {
    val prefsRepo = koinInject<PreferencesRepo>()
    val prefs = prefsRepo.getAllPrefs()

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "Preferences: ",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        prefs.forEach { (k, v) ->
            Text(
                text = "Key: $k | Value: $v",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        OptionsTheme()
    }
}