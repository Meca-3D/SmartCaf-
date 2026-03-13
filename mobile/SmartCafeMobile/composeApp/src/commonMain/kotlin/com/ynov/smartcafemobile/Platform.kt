package com.ynov.smartcafemobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform