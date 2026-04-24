package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.model.CartItem
import com.ynov.smartcafemobile.navigation.Screen
import com.ynov.smartcafemobile.ui.components.BottomNavBar
import com.ynov.smartcafemobile.ui.theme.CafeOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cart: List<CartItem>,
    onIncrease: (CartItem) -> Unit,
    onDecrease: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit,
    onOrder: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var orderSuccess by remember { mutableStateOf(false) }

    val total = cart.sumOf { it.product.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mon Panier 🛒", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Cart,
                cartItemCount = cart.sumOf { it.quantity },
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        if (orderSuccess) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✅", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("Commande envoyée !", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Votre commande est en préparation.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { orderSuccess = false; onNavigate(Screen.Home) },
                        colors = ButtonDefaults.buttonColors(containerColor = CafeOrange)
                    ) {
                        Text("Retour au menu")
                    }
                }
            }
            return@Scaffold
        }

        if (cart.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 64.sp)
                    Spacer(Modifier.height(16.dp))
                    Text("Votre panier est vide", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { onNavigate(Screen.Home) }) {
                        Text("Parcourir le menu", color = CafeOrange)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cart) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { onIncrease(item) },
                            onDecrease = { onDecrease(item) },
                            onRemove = { onRemove(item) }
                        )
                    }
                }

                // Récapitulatif total
                Surface(shadowElevation = 8.dp) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Sous-total", style = MaterialTheme.typography.bodyLarge)
                            Text("${"%.2f".format(total)} €", style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Livraison", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Gratuite ✓", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("${"%.2f".format(total)} €", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = CafeOrange)
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { onOrder(); orderSuccess = true },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CafeOrange)
                        ) {
                            Text("Commander maintenant", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(
                    "${"%.2f".format(item.product.price)} €",
                    style = MaterialTheme.typography.bodySmall,
                    color = CafeOrange
                )
            }
            // Contrôles quantité
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                    Text("−", fontWeight = FontWeight.Bold)
                }
                Text(
                    "${item.quantity}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 24.dp),
                )
                IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                    Text("+", fontWeight = FontWeight.Bold, color = CafeOrange)
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Delete, null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
