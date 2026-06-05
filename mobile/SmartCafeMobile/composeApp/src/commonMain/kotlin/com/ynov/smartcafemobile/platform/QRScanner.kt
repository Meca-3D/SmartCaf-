package com.ynov.smartcafemobile.platform

import androidx.compose.runtime.Composable

/**
 * Platform-specific QR code scanner composable.
 * On Android: uses ZXing camera scanner.
 * On iOS/JVM: falls back to manual text entry.
 */
@Composable
expect fun QRScannerContent(
    onScanned: (String) -> Unit,
    onDismiss: () -> Unit
)
