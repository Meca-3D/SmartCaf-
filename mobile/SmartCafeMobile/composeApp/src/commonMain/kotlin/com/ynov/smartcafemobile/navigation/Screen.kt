package com.ynov.smartcafemobile.navigation

import com.ynov.smartcafemobile.model.Product

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object Home : Screen()
    data class ProductDetail(val product: Product) : Screen()
    object Cart : Screen()
    object Profile : Screen()
}
