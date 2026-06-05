package com.ynov.smartcafemobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// SmartCafé brand palette — warm coffee tones
val CoffeeBrown = Color(0xFF6D4C41)
val CoffeeDark = Color(0xFF4E342E)
val CoffeeLight = Color(0xFF8D6E63)
val Amber = Color(0xFFFFC107)
val AmberDark = Color(0xFFFFA000)
val Cream = Color(0xFFFFF8E1)
val CreamDark = Color(0xFFFFECB3)

private val LightColors = lightColorScheme(
    primary = CoffeeBrown,
    onPrimary = Color.White,
    primaryContainer = CoffeeLight,
    onPrimaryContainer = Color.White,
    secondary = Amber,
    onSecondary = Color.Black,
    secondaryContainer = AmberDark,
    onSecondaryContainer = Color.Black,
    background = Cream,
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun SmartCafeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
