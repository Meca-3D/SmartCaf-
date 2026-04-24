package com.ynov.smartcafemobile

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SmartCafé — Preview Mobile",
        state = WindowState(size = DpSize(390.dp, 844.dp)),
        resizable = true,
    ) {
        App()
    }
}
