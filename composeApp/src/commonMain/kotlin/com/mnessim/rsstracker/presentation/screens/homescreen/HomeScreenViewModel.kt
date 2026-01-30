package com.mnessim.rsstracker.presentation.screens.homescreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnessim.rsstracker.domain.models.Term
import com.mnessim.rsstracker.domain.repositories.ITermsRepo
import com.mnessim.rsstracker.domain.services.IWorkService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val termsRepo: ITermsRepo,
    private val workService: IWorkService,
) : ViewModel() {
    private var _terms = MutableStateFlow<List<Term>>(emptyList())
    val terms: StateFlow<List<Term>> = _terms.asStateFlow()

    private var _showAlert = MutableStateFlow<Boolean>(false)
    val showAlert: StateFlow<Boolean> = _showAlert.asStateFlow()

    val controller = TextFieldState()

    init {
        loadTerms()
        refreshTerms()

    }

    private fun loadTerms() {
        _terms.value = termsRepo.getAllTerms()
    }

    fun addTerm(locked: Boolean) {
        val termName = controller.text.toString()
        if (termName.isEmpty()) return

        termsRepo.insertTerm(termName, locked)
        loadTerms()

    }

    fun removeTerm(id: Long) {
        termsRepo.deleteTerm(id)
        loadTerms()

    }

    fun toggleLocked(term: Term) {
        termsRepo.updateTerm(Term(id = term.id, term = term.term, locked = !term.locked))
        loadTerms()
    }

    fun refreshTerms() {
        viewModelScope.launch {
            workService.refreshWithoutNotification()
            loadTerms()
        }
    }

    fun showAlert() {
        _showAlert.value = true
    }

    fun hideAlert() {
        _showAlert.value = false
    }
}
