package com.mnessim.researchtrackerkmp.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val rssSource: String,
    val title: String,
    val link: String,
    val guid: String? = null,
    @SerialName("atom:link")
    val atomLink: String? = null,
    val description: String? = null,
    @SerialName("author")
    val creator: String? = null,
    val pubDate: String? = null,
    val categories: List<String> = emptyList(),
    val mediaContentUrl: String? = null,
    val mediaContentHeight: Int? = null,
    val mediaContentWidth: Int? = null,
    val mediaCredit: String? = null,
    val mediaDescription: String? = null
)
