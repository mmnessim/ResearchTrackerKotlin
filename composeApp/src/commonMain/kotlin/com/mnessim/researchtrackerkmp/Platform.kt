package com.mnessim.researchtrackerkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform