package com.mnessim.researchtrackerkmp.presentation.screens.homescreen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val termsRepo: ITermsRepo
) : ViewModel() {
    private var _terms = MutableStateFlow<List<Term>>(emptyList())
    val terms: StateFlow<List<Term>> = _terms.asStateFlow()

    val controller = TextFieldState()

    init {
        loadTerms()
    }

    private fun loadTerms() {
        viewModelScope.launch {
            _terms.value = termsRepo.getAllTerms()
        }
    }

    fun addTerm(locked: Boolean) {
        val termName = controller.text.toString()
        if (termName.isEmpty()) return
        viewModelScope.launch {
            termsRepo.insertTerm(termName, locked)
            loadTerms()
        }
    }

    fun removeTerm(id: Long) {
        viewModelScope.launch {
            termsRepo.deleteTerm(id)
            loadTerms()
        }
    }

    fun toggleLocked(term: Term) {
        viewModelScope.launch {
            termsRepo.updateTerm(Term(id = term.id, term = term.term, locked = !term.locked))
            loadTerms()
        }
    }
}
