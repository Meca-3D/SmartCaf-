package com.ynov.smartcafemobile.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Simple window-size classification usable on all KMP targets.
 * Mirrors the Material3 WindowSizeClass without the Android-only dependency.
 */
enum class WindowSize { Compact, Medium, Expanded }

@Composable
fun rememberWindowSize(windowWidthDp: Dp): WindowSize = when {
    windowWidthDp < 600.dp -> WindowSize.Compact
    windowWidthDp < 840.dp -> WindowSize.Medium
    else -> WindowSize.Expanded
}

/** Horizontal content padding that grows with screen width */
fun WindowSize.horizontalPadding(): Dp = when (this) {
    WindowSize.Compact  -> 16.dp
    WindowSize.Medium   -> 32.dp
    WindowSize.Expanded -> 64.dp
}

/** Number of columns for product grid */
fun WindowSize.gridColumns(): Int = when (this) {
    WindowSize.Compact  -> 2
    WindowSize.Medium   -> 3
    WindowSize.Expanded -> 4
}
