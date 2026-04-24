package com.ynov.smartcafemobile.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null
)
