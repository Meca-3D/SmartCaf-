package com.ynov.smartcafemobile.model

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)
