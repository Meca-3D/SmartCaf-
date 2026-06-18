package com.ynov.smartcafemobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// SmartCafé brand palette — Figma design
val Gold        = Color(0xFFB08919)
val GoldLight   = Color(0xFFC9A040)
val DarkGreen   = Color(0xFF0F3D2E)
val Beige       = Color(0xFFFFEABD)
val BeigeLight  = Color(0xFFFFF5D6)
val BrandRed    = Color(0xFFC0392B)
val BrandText   = Color(0xFF3A2F2A)

private val SmartCafeColors = lightColorScheme(
    primary             = DarkGreen,
    onPrimary           = Color.White,
    primaryContainer    = Gold,
    onPrimaryContainer  = Color.White,
    secondary           = Gold,
    onSecondary         = Color.White,
    secondaryContainer  = GoldLight,
    onSecondaryContainer = Color.White,
    background          = Beige,
    onBackground        = BrandText,
    surface             = BeigeLight,
    onSurface           = BrandText,
    surfaceVariant      = Color(0xFFEDD99A),
    onSurfaceVariant    = BrandText,
    error               = BrandRed,
    onError             = Color.White
)

@Composable
fun SmartCafeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SmartCafeColors,
        content = content
    )
}
