package com.mnessim.researchtrackerkmp

import kotlinx.serialization.Serializable

interface AppRoute

@Serializable
object HomeRoute : AppRoute

@Serializable
data class DetailsRoute(val id: Long) : AppRoute

@Serializable
object NavTilesRoute : AppRoute