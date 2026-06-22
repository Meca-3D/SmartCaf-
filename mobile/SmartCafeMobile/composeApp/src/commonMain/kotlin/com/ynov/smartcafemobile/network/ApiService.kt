package com.ynov.smartcafemobile.network

import com.ynov.smartcafemobile.model.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object ApiService {

    suspend fun getProducts(): List<Product> =
        httpClient.get("$BASE_URL/api/products").body()

    suspend fun getProductsByCategory(category: String): List<Product> =
        httpClient.get("$BASE_URL/api/products/category/$category").body()

    suspend fun login(email: String, password: String): AuthResponse =
        httpClient.post("$BASE_URL/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Map<String, String> =
        httpClient.post("$BASE_URL/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(firstName, lastName, email, password))
        }.body()

    suspend fun createOrder(request: CreateOrderRequest): Order =
        httpClient.post("$BASE_URL/api/orders") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun getOrder(id: Long): Order =
        httpClient.get("$BASE_URL/api/orders/$id").body()

    suspend fun checkBanStatus(userId: Long): Boolean {
        val response: Map<String, Boolean> = httpClient.get("$BASE_URL/api/auth/check/$userId").body()
        return response["banned"] == true
    }

    suspend fun deleteUser(userId: Long) {
        httpClient.delete("$BASE_URL/api/admin/users/$userId")
    }
}
