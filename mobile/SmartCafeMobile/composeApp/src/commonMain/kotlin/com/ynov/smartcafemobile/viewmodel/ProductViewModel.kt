package com.ynov.smartcafemobile.viewmodel

import com.ynov.smartcafemobile.model.Product
import com.ynov.smartcafemobile.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        loadProducts()
    }

    fun loadProducts() {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                allProducts = ApiService.getProducts().filter { it.isActive }
                applyFilter()
            } catch (e: Exception) {
                _error.value = "Impossible de charger les produits : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
        applyFilter()
    }

    private fun applyFilter() {
        _products.value = if (_selectedCategory.value == null) {
            allProducts
        } else {
            allProducts.filter { it.category == _selectedCategory.value }
        }
    }

    fun getCategories(): List<String> =
        allProducts.mapNotNull { it.category }.distinct().sorted()
}
