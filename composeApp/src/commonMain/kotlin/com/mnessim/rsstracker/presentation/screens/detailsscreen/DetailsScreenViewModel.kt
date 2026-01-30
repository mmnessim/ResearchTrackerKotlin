package com.mnessim.rsstracker.presentation.screens.detailsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnessim.rsstracker.domain.models.Article
import com.mnessim.rsstracker.domain.models.Term
import com.mnessim.rsstracker.domain.repositories.ITermsRepo
import com.mnessim.rsstracker.domain.repositories.PreferencesRepo
import com.mnessim.rsstracker.domain.services.ApiService
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
    private val apiService: ApiService,
    private val prefsRepo: PreferencesRepo
) : ViewModel() {

    private var _response = MutableStateFlow<List<Article>>(emptyList())
    val response: StateFlow<List<Article>> = _response.asStateFlow()

    private var _loading = MutableStateFlow<Boolean>(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var _blocked = MutableStateFlow<List<String>>(emptyList())
    val blocked: StateFlow<List<String>> = _blocked.asStateFlow()

    var term: Term = termsRepo.getTermById(id) ?: Term(-1, "Error Loading Term", false)

    init {
        // Reset hasNewArticle to false when viewing details
        if (term.hasNewArticle) {
            termsRepo.updateTerm(
                Term(
                    id = term.id,
                    term = term.term,
                    locked = term.locked,
                    lastArticleGuid = term.lastArticleGuid,
                    hasNewArticle = false
                )
            )
        }
        loadBlocked()
    }

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

    fun loadBlocked() {
        _blocked.value = prefsRepo.getPrefByKey("blockedFeeds")?.split(",") ?: emptyList<String>()
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