package com.ynov.smartcafemobile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    val price: Double = 0.0,
    val stock: Int = 0,
    val imageUrl: String? = null,
    @SerialName("isActive") val isActive: Boolean = true,
    val category: String? = null
)
