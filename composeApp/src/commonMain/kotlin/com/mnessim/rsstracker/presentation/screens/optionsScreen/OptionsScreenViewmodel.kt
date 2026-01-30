package com.mnessim.rsstracker.presentation.screens.optionsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnessim.rsstracker.domain.repositories.PreferencesRepo
import com.mnessim.rsstracker.domain.services.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OptionsScreenViewmodel(
    private val prefsRepo: PreferencesRepo,
    private val apiService: ApiService
) : ViewModel() {

    private var _feeds = MutableStateFlow<List<String>>(emptyList())
    val feeds: StateFlow<List<String>> = _feeds.asStateFlow()

    private var _blocked = MutableStateFlow<List<String>>(emptyList())
    val blocked: StateFlow<List<String>> = _blocked.asStateFlow()

    init {
        viewModelScope.launch {
            _feeds.value = apiService.getFeedsList()
        }
        loadPrefs()
    }

    fun loadPrefs() {
        val blockedPrefs = prefsRepo.getPrefByKey("blockedFeeds")
        if (blockedPrefs == null) {
            prefsRepo.insertPref("blockedFeeds", "")
        } else {
            _blocked.value = blockedPrefs.split(",")
        }
    }

    fun togglePref(term: String) {
        loadPrefs()
        if (_blocked.value.isEmpty()) {
            prefsRepo.updatePref("blockedFeeds", term)
            return
        }
        if (_blocked.value.contains(term)) {
            _blocked.value -= term
            prefsRepo.updatePref("blockedFeeds", _blocked.value.joinToString(","))
        } else {
            _blocked.value += term
            prefsRepo.updatePref("blockedFeeds", _blocked.value.joinToString(","))
        }
    }

}