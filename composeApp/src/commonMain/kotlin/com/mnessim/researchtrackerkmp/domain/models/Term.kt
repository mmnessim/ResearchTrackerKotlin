package com.mnessim.researchtrackerkmp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Term(
    val id: Long,
    val term: String,
    val locked: Boolean,
    val lastArticleGuid: String? = null
    // TODO: implement new field, show UI element if true on HomeScreen
    // val hasNewArticle: Boolean = false
)
