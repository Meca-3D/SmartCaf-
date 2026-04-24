package com.ynov.smartcafemobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Palette SmartCafé
val CafeOrange     = Color(0xFFE8813A)
val CafeOrangeLight = Color(0xFFFFDBC8)
val CafeBrown      = Color(0xFF6B4C3B)
val CafeBrownDark  = Color(0xFF2C1810)
val CafeCream      = Color(0xFFFFF8F3)
val CafeGreen      = Color(0xFF4CAF50)

private val LightColors = lightColorScheme(
    primary          = CafeOrange,
    onPrimary        = Color.White,
    primaryContainer = CafeOrangeLight,
    onPrimaryContainer = CafeBrownDark,
    secondary        = CafeBrown,
    onSecondary      = Color.White,
    background       = CafeCream,
    onBackground     = CafeBrownDark,
    surface          = Color.White,
    onSurface        = CafeBrownDark,
    surfaceVariant   = Color(0xFFF5EDE7),
    onSurfaceVariant = CafeBrown,
    error            = Color(0xFFB00020),
    onError          = Color.White,
)

@Composable
fun SmartCafeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
