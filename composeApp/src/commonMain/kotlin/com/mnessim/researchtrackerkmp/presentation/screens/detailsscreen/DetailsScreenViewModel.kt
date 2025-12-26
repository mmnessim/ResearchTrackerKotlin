package com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnessim.researchtrackerkmp.domain.models.Article
import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.domain.services.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    id: Long,
    termsRepo: ITermsRepo,
    private val apiService: ApiService
) : ViewModel() {

    private var _response = MutableStateFlow<List<Article>>(emptyList())
    val response: StateFlow<List<Article>> = _response.asStateFlow()

    var term: Term = termsRepo.getTermById(id) ?: Term(-1, "Error Loading Term", false)

    fun fetch() {
        viewModelScope.launch {
            println("API called")
            _response.value = apiService.search(term.term)
        }
    }

    fun sort(by: String) {
        when (by) {
            "source" -> _response.value = _response.value.sortedBy { article -> article.rssSource }
            else -> return
        }

    }
}