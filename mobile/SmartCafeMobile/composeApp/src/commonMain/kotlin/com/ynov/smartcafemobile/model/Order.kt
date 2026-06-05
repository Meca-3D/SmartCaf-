package com.ynov.smartcafemobile.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Long = 0,
    val customerName: String = "",
    val orderType: String = "CLICK_AND_COLLECT",
    val status: String = "PENDING",
    val totalPrice: Double = 0.0,
    val tableId: Long? = null,
    val items: List<OrderItemResponse> = emptyList()
)

@Serializable
data class OrderItemResponse(
    val id: Long = 0,
    val quantity: Int = 0,
    val price: Double = 0.0,
    val product: Product? = null
)

@Serializable
data class CreateOrderRequest(
    val customerName: String,
    val orderType: String,
    val tableId: Long?,
    val items: List<CreateOrderItemRequest>
)

@Serializable
data class CreateOrderItemRequest(
    val productId: Long,
    val quantity: Int
)
