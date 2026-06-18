package com.ynov.smartcafemobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(size = DpSize(430.dp, 860.dp))
    Window(
        onCloseRequest = ::exitApplication,
        title = "SmartCafé Mobile",
        state = state,
        resizable = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A2E)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(390.dp)
                    .fillMaxHeight()
            ) {
                App()
            }
        }
    }
}