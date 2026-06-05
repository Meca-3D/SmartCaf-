package com.ynov.smartcafemobile.platform

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
actual fun QRScannerContent(
    onScanned: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        val content = result.contents
        if (content != null) {
            onScanned(content)
        } else {
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        val options = ScanOptions()
            .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            .setPrompt("Scannez le QR code de votre table")
            .setBeepEnabled(true)
            .setOrientationLocked(false)
        launcher.launch(options)
    }

    // Shown briefly while the scanner activity launches
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator()
            Text("Ouverture du scanner…")
            OutlinedButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    }
}
