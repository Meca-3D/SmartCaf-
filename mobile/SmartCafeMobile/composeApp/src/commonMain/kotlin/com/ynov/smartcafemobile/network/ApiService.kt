package com.ynov.smartcafemobile.network

import com.ynov.smartcafemobile.model.AuthResponse
import com.ynov.smartcafemobile.model.Category
import com.ynov.smartcafemobile.model.LoginRequest
import com.ynov.smartcafemobile.model.Product
import com.ynov.smartcafemobile.model.RegisterRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

// Pour Android emulator : http://10.0.2.2:8081
// Pour JVM desktop  : http://localhost:8081
// Pour device physique : http://<IP_MACHINE>:8081
private const val BASE_URL = "http://localhost:8081"

class ApiService {
    private val client = createHttpClient()

    suspend fun login(email: String, password: String): Result<AuthResponse> = runCatching {
        client.post("$BASE_URL/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }

    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> = runCatching {
        client.post("$BASE_URL/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(name, email, password))
        }.body()
    }

    suspend fun getProducts(): Result<List<Product>> = runCatching {
        client.get("$BASE_URL/api/products").body()
    }

    suspend fun getProductsByCategory(category: String): Result<List<Product>> = runCatching {
        client.get("$BASE_URL/api/products/category/$category").body()
    }

    suspend fun searchProducts(query: String): Result<List<Product>> = runCatching {
        client.get("$BASE_URL/api/products/search?name=$query").body()
    }

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        client.get("$BASE_URL/api/categories").body()
    }

}
