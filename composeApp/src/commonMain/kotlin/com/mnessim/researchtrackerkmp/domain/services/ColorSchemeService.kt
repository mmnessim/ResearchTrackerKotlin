package com.mnessim.researchtrackerkmp.domain.services

import androidx.compose.material3.ColorScheme
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import com.mnessim.researchtrackerkmp.presentation.theme.darkScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastDarkColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastLightColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ColorSchemeService : KoinComponent {
    private val repo by inject<PreferencesRepo>()

    private val schemeKey = "colorScheme"

    private val _scheme = MutableStateFlow(lightScheme)
    val scheme: StateFlow<ColorScheme> = _scheme

    init {
        val current = repo.getPrefByKey(schemeKey)
        _scheme.value = mapKeyToScheme(current)
    }

    fun setScheme(newScheme: String) {
        val existing = repo.getPrefByKey(schemeKey)
        if (existing == null) {
            repo.insertPref(schemeKey, newScheme)
        } else {
            repo.updatePref(schemeKey, newScheme)
        }
        _scheme.value = mapKeyToScheme(newScheme)
    }

    private fun mapKeyToScheme(key: String?): androidx.compose.material3.ColorScheme {
        return when (key) {
            "dark" -> darkScheme
            "lightContrast" -> highContrastLightColorScheme
            "darkContrast" -> highContrastDarkColorScheme
            "light", null -> lightScheme
            else -> lightScheme
        }
    }
}