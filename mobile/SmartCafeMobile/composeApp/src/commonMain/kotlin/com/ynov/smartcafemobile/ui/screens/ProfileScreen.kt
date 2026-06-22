package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.ui.theme.Beige
import com.ynov.smartcafemobile.ui.theme.BrandText
import com.ynov.smartcafemobile.ui.theme.DarkGreen
import com.ynov.smartcafemobile.ui.theme.Gold
import com.ynov.smartcafemobile.viewmodel.AuthViewModel

private fun formatAndCursor(
    newValue: TextFieldValue,
    maxDigits: Int,
    separators: List<Pair<Int, Char>>
): TextFieldValue {
    val digits = newValue.text.filter { it.isDigit() }.take(maxDigits)
    val formatted = buildString {
        digits.forEachIndexed { i, c ->
            separators.forEach { (pos, sep) -> if (i == pos) append(sep) }
            append(c)
        }
    }
    val digitsBeforeCursor = newValue.text.take(newValue.selection.start).count { it.isDigit() }.coerceAtMost(digits.length)
    var counted = 0
    var cursorPos = formatted.length
    for ((i, c) in formatted.withIndex()) {
        if (c.isDigit()) {
            counted++
            if (counted == digitsBeforeCursor) {
                cursorPos = i + 1
                break
            }
        }
    }
    if (digitsBeforeCursor == 0) cursorPos = 0
    return TextFieldValue(formatted, TextRange(cursorPos))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    var firstName by remember(currentUser) { mutableStateOf(currentUser?.firstName ?: "") }
    var lastName by remember(currentUser) { mutableStateOf(currentUser?.lastName ?: "") }
    var email by remember(currentUser) { mutableStateOf(currentUser?.email ?: "") }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var birthDate by remember { mutableStateOf(TextFieldValue("")) }

    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Modifications enregistrées", fontWeight = FontWeight.Bold, color = BrandText) },
            text = { Text("Vos informations ont bien été mises à jour.", color = BrandText) },
            confirmButton = {
                Button(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Gold)
                ) { Text("OK") }
            },
            containerColor = Beige
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Café", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Gold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = 12.dp).size(36.dp).clip(CircleShape).background(DarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGreen)
            )
        },
        containerColor = Beige
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(24.dp))
            Text("Mon profil", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = BrandText, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Gold.copy(alpha = 0.3f))
            Spacer(Modifier.height(16.dp))

            EditableProfileField(label = "Prénom", value = firstName, onValueChange = { firstName = it })
            EditableProfileField(label = "Nom", value = lastName, onValueChange = { lastName = it })
            EditableProfileField(label = "Mail", value = email, onValueChange = { email = it })

            // Téléphone — chiffres uniquement, format XX XX XX XX XX
            FormattedProfileField(
                label = "Tél",
                value = phone,
                onValueChange = { new ->
                    phone = formatAndCursor(new, maxDigits = 10, separators = listOf(2 to ' ', 4 to ' ', 6 to ' ', 8 to ' '))
                },
                keyboardType = KeyboardType.Number,
                placeholder = "06 12 34 56 78"
            )

            // Date de naissance — chiffres uniquement, format JJ/MM/AAAA
            FormattedProfileField(
                label = "Date de naissance",
                value = birthDate,
                onValueChange = { new ->
                    birthDate = formatAndCursor(new, maxDigits = 8, separators = listOf(2 to '/', 4 to '/'))
                },
                keyboardType = KeyboardType.Number,
                placeholder = "JJ/MM/AAAA"
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold)
            ) {
                Text("Enregistrer les modifications", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholder: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = BrandText.copy(alpha = 0.7f)) },
            placeholder = if (placeholder != null) {{ Text(placeholder, color = BrandText.copy(alpha = 0.35f)) }} else null,
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(18.dp)) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Gold.copy(alpha = 0.4f),
                cursorColor = Gold,
                focusedTextColor = BrandText,
                unfocusedTextColor = BrandText
            ),
            singleLine = true
        )
    }
}

@Composable
private fun FormattedProfileField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardType: KeyboardType,
    placeholder: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = BrandText.copy(alpha = 0.7f)) },
            placeholder = { Text(placeholder, color = BrandText.copy(alpha = 0.35f)) },
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(18.dp)) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold,
                unfocusedBorderColor = Gold.copy(alpha = 0.4f),
                cursorColor = Gold,
                focusedTextColor = BrandText,
                unfocusedTextColor = BrandText
            ),
            singleLine = true
        )
    }
}
