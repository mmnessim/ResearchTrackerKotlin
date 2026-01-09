package com.mnessim.researchtrackerkmp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val rssSource: String,
    val title: String,
    val link: String,
    val guid: String? = null,
    val description: String? = null,
    // TODO: when switching to Rust backend, this will be millis since epoch instead of string
    val pubDate: String? = null,
    val pubDateMs: Long? = null,
    val categories: List<String> = emptyList(),
)
