package com.ynov.smartcafemobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.ynov.smartcafemobile.navigation.Screen

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    cartItemCount: Int,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Accueil") },
            label = { Text("Accueil") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Cart,
            onClick = { onNavigate(Screen.Cart) },
            icon = {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge { Text("$cartItemCount") }
                        }
                    }
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Panier")
                }
            },
            label = { Text("Panier") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") },
            label = { Text("Profil") }
        )
    }
}
