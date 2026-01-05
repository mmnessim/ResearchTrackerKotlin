package com.mnessim.researchtrackerkmp.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    @SerialName("num_articles")
    val numArticles: Int,
    @SerialName("num_sources")
    val numSources: Int
)
