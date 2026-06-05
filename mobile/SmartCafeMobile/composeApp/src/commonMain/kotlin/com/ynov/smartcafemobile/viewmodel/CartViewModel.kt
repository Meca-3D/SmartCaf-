package com.ynov.smartcafemobile.viewmodel

import com.ynov.smartcafemobile.model.CartItem
import com.ynov.smartcafemobile.model.CreateOrderItemRequest
import com.ynov.smartcafemobile.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalPrice: Double
        get() = _items.value.sumOf { it.product.price * it.quantity }

    val totalItems: StateFlow<Int> = _items
        .map { list -> list.sumOf { it.quantity } }
        .stateIn(scope, SharingStarted.Eagerly, 0)

    fun addItem(product: Product) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.product.id == product.id }
        if (idx >= 0) {
            current[idx] = current[idx].copy(quantity = current[idx].quantity + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _items.value = current.toList()
    }

    fun decreaseItem(productId: Long) {
        val current = _items.value.toMutableList()
        val idx = current.indexOfFirst { it.product.id == productId }
        if (idx >= 0) {
            if (current[idx].quantity > 1) {
                current[idx] = current[idx].copy(quantity = current[idx].quantity - 1)
            } else {
                current.removeAt(idx)
            }
        }
        _items.value = current.toList()
    }

    fun removeItem(productId: Long) {
        _items.value = _items.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    fun toOrderItems(): List<CreateOrderItemRequest> =
        _items.value.map { CreateOrderItemRequest(it.product.id, it.quantity) }
}
