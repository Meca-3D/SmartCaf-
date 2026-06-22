package com.ynov.smartcafemobile.viewmodel

import com.ynov.smartcafemobile.model.CreateOrderRequest
import com.ynov.smartcafemobile.model.CreateOrderItemRequest
import com.ynov.smartcafemobile.model.Order
import com.ynov.smartcafemobile.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _lastOrder = MutableStateFlow<Order?>(null)
    val lastOrder: StateFlow<Order?> = _lastOrder.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    init {
        scope.launch {
            while (true) {
                delay(5000)
                val toUpdate = _orders.value.filter {
                    it.status == "PENDING" || it.status == "IN_PROGRESS"
                }
                for (order in toUpdate) {
                    try {
                        val updated = ApiService.getOrder(order.id)
                        _orders.value = _orders.value.map { if (it.id == updated.id) updated else it }
                    } catch (_: Exception) {}
                }
            }
        }
    }

    fun placeOrder(
        customerName: String,
        orderType: String,
        tableId: Long?,
        items: List<CreateOrderItemRequest>,
        onSuccess: (Long) -> Unit
    ) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val request = CreateOrderRequest(
                    customerName = customerName,
                    orderType = orderType,
                    tableId = tableId,
                    items = items
                )
                val order = ApiService.createOrder(request)
                _lastOrder.value = order
                _orders.value = _orders.value + order
                onSuccess(order.id)
            } catch (e: Exception) {
                _error.value = "Impossible de passer la commande : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
