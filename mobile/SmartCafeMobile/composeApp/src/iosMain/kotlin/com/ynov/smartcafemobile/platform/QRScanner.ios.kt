package com.ynov.smartcafemobile.platform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
actual fun QRScannerContent(
    onScanned: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var tableNumber by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.padding(32.dp)) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Entrez le numéro de table", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = tableNumber,
                    onValueChange = { tableNumber = it.filter { c -> c.isDigit() } },
                    label = { Text("N° de table") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss) { Text("Annuler") }
                    Button(
                        onClick = { if (tableNumber.isNotBlank()) onScanned(tableNumber) },
                        enabled = tableNumber.isNotBlank()
                    ) { Text("Confirmer") }
                }
            }
        }
    }
}
