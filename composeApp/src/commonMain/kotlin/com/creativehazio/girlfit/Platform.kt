package com.creativehazio.girlfit

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform