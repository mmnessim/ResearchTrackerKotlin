package com.mnessim.researchtrackerkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val isIos: Boolean