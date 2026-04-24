package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.network.ApiService
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.ui.theme.CafeOrange
import com.ynov.smartcafemobile.ui.theme.CafeCream
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onGoToRegister: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CafeCream),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Text("☕", fontSize = 72.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "SmartCafé",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = CafeOrange
            )
            Text(
                "Votre café connecté",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(40.dp))

            // Card formulaire
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "Connexion",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Mot de passe") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    errorMessage?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                errorMessage = null
                                val result = ApiService().login(email.trim(), password)
                                result.onSuccess { resp ->
                                    if (resp.user != null) {
                                        onLoginSuccess(resp.user)
                                    } else {
                                        // Connexion demo sans backend
                                        onLoginSuccess(User(name = email.substringBefore("@"), email = email))
                                    }
                                }.onFailure {
                                    // Mode démo : connexion sans backend
                                    if (email.isNotBlank() && password.isNotBlank()) {
                                        onLoginSuccess(User(name = email.substringBefore("@"), email = email))
                                    } else {
                                        errorMessage = "Veuillez remplir tous les champs"
                                    }
                                }
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = CafeOrange)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Se connecter", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onGoToRegister) {
                Text("Pas encore de compte ? ")
                Text("S'inscrire", fontWeight = FontWeight.Bold, color = CafeOrange)
            }
        }
    }
}
