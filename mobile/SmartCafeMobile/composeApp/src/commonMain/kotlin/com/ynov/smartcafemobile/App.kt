package com.ynov.smartcafemobile

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ynov.smartcafemobile.model.CartItem
import com.ynov.smartcafemobile.model.Product
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.navigation.Screen
import com.ynov.smartcafemobile.ui.screens.*
import com.ynov.smartcafemobile.ui.theme.SmartCafeTheme

@Composable
fun App() {
    val cart = remember { mutableStateListOf<CartItem>() }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    var currentUser by remember { mutableStateOf<User?>(null) }

    fun addToCart(product: Product, qty: Int = 1) {
        val existing = cart.indexOfFirst { it.product.id == product.id }
        if (existing >= 0) {
            cart[existing] = cart[existing].copy(quantity = cart[existing].quantity + qty)
        } else {
            cart.add(CartItem(product, qty))
        }
    }

    SmartCafeTheme {
        when (val screen = currentScreen) {
            is Screen.Login -> LoginScreen(
                onLoginSuccess = { user ->
                    currentUser = user
                    currentScreen = Screen.Home
                },
                onGoToRegister = { currentScreen = Screen.Register }
            )

            is Screen.Register -> RegisterScreen(
                onRegisterSuccess = { user ->
                    currentUser = user
                    currentScreen = Screen.Home
                },
                onGoToLogin = { currentScreen = Screen.Login }
            )

            is Screen.Home -> HomeScreen(
                user = currentUser ?: User(),
                cart = cart,
                onAddToCart = { product -> addToCart(product) },
                onProductClick = { product -> currentScreen = Screen.ProductDetail(product) },
                onNavigate = { currentScreen = it }
            )

            is Screen.ProductDetail -> ProductDetailScreen(
                product = screen.product,
                onBack = { currentScreen = Screen.Home },
                onAddToCart = { product, qty ->
                    addToCart(product, qty)
                    currentScreen = Screen.Cart
                }
            )

            is Screen.Cart -> CartScreen(
                cart = cart,
                onIncrease = { item ->
                    val idx = cart.indexOf(item)
                    if (idx >= 0) cart[idx] = cart[idx].copy(quantity = cart[idx].quantity + 1)
                },
                onDecrease = { item ->
                    val idx = cart.indexOf(item)
                    if (idx >= 0) {
                        if (cart[idx].quantity > 1) cart[idx] = cart[idx].copy(quantity = cart[idx].quantity - 1)
                        else cart.removeAt(idx)
                    }
                },
                onRemove = { item -> cart.remove(item) },
                onOrder = { cart.clear() },
                onNavigate = { currentScreen = it }
            )

            is Screen.Profile -> ProfileScreen(
                user = currentUser ?: User(),
                cartItemCount = cart.sumOf { it.quantity },
                onLogout = {
                    currentUser = null
                    cart.clear()
                    currentScreen = Screen.Login
                },
                onNavigate = { currentScreen = it }
            )
        }
    }
}
