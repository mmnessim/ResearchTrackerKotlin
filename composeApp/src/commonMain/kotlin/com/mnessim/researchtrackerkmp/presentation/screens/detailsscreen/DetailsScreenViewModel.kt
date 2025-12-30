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
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class DetailsScreenViewModel(
    id: Long,
    private val termsRepo: ITermsRepo,
    private val apiService: ApiService
) : ViewModel() {

    private var _response = MutableStateFlow<List<Article>>(emptyList())
    val response: StateFlow<List<Article>> = _response.asStateFlow()

    private var _loading = MutableStateFlow<Boolean>(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    var term: Term = termsRepo.getTermById(id) ?: Term(-1, "Error Loading Term", false)

    fun fetch() {
        viewModelScope.launch {
            println("API called")
            _response.value = apiService.search(term.term)
            _loading.value = false
            updateGuid()
        }
    }

    fun sort(by: String) {
        // TODO: add more options
        when (by) {
            "source" -> _response.value = _response.value.sortedBy { article -> article.rssSource }
            "date" -> _response.value =
                _response.value.sortedByDescending { article -> parsePubDate(article.pubDate) }

            else -> return
        }
    }

    private fun updateGuid() {
        if (_response.value.isNotEmpty()) {
            viewModelScope.launch {
                val guid = _response.value.first().guid
                if (guid != term.lastArticleGuid) {
                    println("Updating GUID")
                    termsRepo.updateTerm(
                        Term(
                            id = term.id,
                            term = term.term,
                            locked = term.locked,
                            lastArticleGuid = guid
                        )
                    )
                }
            }
        }
    }

    private fun parsePubDate(pubDate: String?): Long {
        if (pubDate.isNullOrBlank()) return Long.MIN_VALUE
        return try {
            Instant.parse(pubDate).toEpochMilliseconds()
        } catch (e: Exception) {
            pubDate.toLongOrNull() ?: Long.MIN_VALUE
        }
    }
}