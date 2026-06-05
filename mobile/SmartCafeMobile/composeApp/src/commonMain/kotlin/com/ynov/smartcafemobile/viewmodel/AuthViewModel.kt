package com.ynov.smartcafemobile.viewmodel

import com.ynov.smartcafemobile.model.AuthResponse
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response: AuthResponse = ApiService.login(email, password)
                if (response.user != null) {
                    _currentUser.value = response.user
                    onSuccess()
                } else {
                    _error.value = response.message.ifBlank { "Email ou mot de passe incorrect" }
                }
            } catch (e: Exception) {
                _error.value = "Connexion impossible : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiService.register(firstName, lastName, email, password)
                val message = response["message"] ?: ""
                if (message.contains("réussie", ignoreCase = true) || message.contains("success", ignoreCase = true)) {
                    // Auto-login after registration
                    val authResp: AuthResponse = ApiService.login(email, password)
                    if (authResp.user != null) {
                        _currentUser.value = authResp.user
                        onSuccess()
                    } else {
                        _error.value = "Inscription réussie. Veuillez vous connecter."
                    }
                } else {
                    _error.value = message.ifBlank { "Erreur lors de l'inscription" }
                }
            } catch (e: Exception) {
                _error.value = "Inscription impossible : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun logout() {
        _currentUser.value = null
    }
}
