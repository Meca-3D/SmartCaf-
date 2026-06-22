package com.ynov.smartcafemobile.viewmodel

import com.ynov.smartcafemobile.model.AuthResponse
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
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

    private val _isBanned = MutableStateFlow(false)
    val isBanned: StateFlow<Boolean> = _isBanned.asStateFlow()

    private var banCheckJob: Job? = null

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response: AuthResponse = ApiService.login(email, password)
                if (response.user != null) {
                    _currentUser.value = response.user
                    startBanPolling()
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
                    val authResp: AuthResponse = ApiService.login(email, password)
                    if (authResp.user != null) {
                        _currentUser.value = authResp.user
                        startBanPolling()
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

    private fun startBanPolling() {
        banCheckJob?.cancel()
        banCheckJob = scope.launch {
            while (true) {
                delay(10_000)
                val userId = _currentUser.value?.id ?: break
                try {
                    val banned = ApiService.checkBanStatus(userId)
                    if (banned) {
                        _isBanned.value = true
                        _currentUser.value = null
                        break
                    }
                } catch (_: Exception) {}
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearBanned() {
        _isBanned.value = false
    }

    fun logout() {
        banCheckJob?.cancel()
        banCheckJob = null
        _isBanned.value = false
        _currentUser.value = null
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val userId = _currentUser.value?.id ?: return@launch
                ApiService.deleteUser(userId)
                logout()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Impossible de supprimer le compte : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
