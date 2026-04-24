package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onGoToLogin: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CafeCream)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Créer un compte") },
                navigationIcon = {
                    IconButton(onClick = onGoToLogin) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CafeCream,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                    Text("☕", fontSize = 48.sp)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; errorMessage = null },
                            label = { Text("Nom complet") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(Modifier.height(12.dp))
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
                                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                                    errorMessage = "Veuillez remplir tous les champs"
                                    return@Button
                                }
                                scope.launch {
                                    isLoading = true
                                    val result = ApiService().register(name.trim(), email.trim(), password)
                                    result.onSuccess {
                                        onRegisterSuccess(User(name = name, email = email))
                                    }.onFailure {
                                        // Mode démo sans backend
                                        onRegisterSuccess(User(name = name, email = email))
                                    }
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isLoading,
                            colors = ButtonDefaults.buttonColors(containerColor = CafeOrange)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("S'inscrire", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                TextButton(onClick = onGoToLogin) {
                    Text("Déjà un compte ? ")
                    Text("Se connecter", fontWeight = FontWeight.Bold, color = CafeOrange)
                }
            }
        }
    }
}
