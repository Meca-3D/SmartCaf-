package com.ynov.smartcafemobile.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.*
import io.ktor.serialization.kotlinx.json.*

// Android emulator: 10.0.2.2 pointe vers le localhost de la machine hôte
// Sur device physique, remplace par l'IP de ta machine (ex: 192.168.1.X)
actual val BASE_URL = "http://10.0.2.2:8081"

actual fun createHttpClient(): HttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        connectTimeoutMillis = 10_000
        requestTimeoutMillis = 15_000
        socketTimeoutMillis = 15_000
    }
    engine {
        connectTimeout = 10_000
        socketTimeout = 15_000
    }
}
