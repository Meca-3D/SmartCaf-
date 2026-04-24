package com.ynov.smartcafemobile.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val role: String = "CLIENT"
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val message: String = "",
    val user: User? = null
)
