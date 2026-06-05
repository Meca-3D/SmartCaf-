package com.ynov.smartcafemobile.network

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// Platform-specific engine instantiation
expect fun createHttpClient(): HttpClient

// Platform-specific backend URL
expect val BASE_URL: String

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
}

val httpClient: HttpClient by lazy {
    createHttpClient()
}
