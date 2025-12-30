package com.mnessim.researchtrackerkmp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Term(
    val id: Long,
    val term: String,
    val locked: Boolean,
    val lastArticleGuid: String? = null
)
