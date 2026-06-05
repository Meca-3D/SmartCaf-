package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.ui.theme.CoffeeBrown
import com.ynov.smartcafemobile.viewmodel.CartViewModel
import com.ynov.smartcafemobile.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    currentUser: User?,
    orderType: String,    // "ON_SITE" or "CLICK_AND_COLLECT"
    tableId: Long?,
    onPaymentSuccess: (Long) -> Unit,
    onBack: () -> Unit
) {
    val cartItems by cartViewModel.items.collectAsState()
    val totalPrice by remember { derivedStateOf { cartViewModel.totalPrice } }
    val isLoading by orderViewModel.isLoading.collectAsState()
    val error by orderViewModel.error.collectAsState()

    var selectedPaymentMethod by remember { mutableStateOf("Carte bancaire") }
    val paymentMethods = listOf("Carte bancaire", "Apple Pay / Google Pay", "Espèces au comptoir")

    LaunchedEffect(Unit) { orderViewModel.clearError() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Paiement", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeBrown,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order type recap
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            if (orderType == "ON_SITE") "🪑 Sur place" else "🥡 Click & Collect",
                            fontWeight = FontWeight.Bold,
                            color = CoffeeBrown
                        )
                        if (tableId != null && tableId > 0) {
                            Text("Table n°$tableId", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Order summary
            Text("Récapitulatif", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Card(shape = RoundedCornerShape(12.dp)) {
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(cartItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${item.quantity}x ${item.product.name}", modifier = Modifier.weight(1f))
                            Text("${"%.2f".format(item.product.price * item.quantity)} €", fontWeight = FontWeight.SemiBold)
                        }
                    }
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "${"%.2f".format(totalPrice)} €",
                                fontWeight = FontWeight.Bold,
                                color = CoffeeBrown,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }

            // Payment method selector
            Text("Mode de paiement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            paymentMethods.forEach { method ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == method,
                        onClick = { selectedPaymentMethod = method }
                    )
                    Text(method, modifier = Modifier.padding(start = 8.dp))
                }
            }

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.weight(1f))

            // Confirm payment button
            val customerName = "${currentUser?.firstName ?: ""} ${currentUser?.lastName ?: ""}".trim()
                .ifBlank { "Client" }

            Button(
                onClick = {
                    orderViewModel.placeOrder(
                        customerName = customerName,
                        orderType = orderType,
                        tableId = tableId,
                        items = cartViewModel.toOrderItems(),
                        onSuccess = { orderId ->
                            cartViewModel.clearCart()
                            onPaymentSuccess(orderId)
                        }
                    )
                },
                enabled = !isLoading && cartItems.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Traitement en cours…")
                } else {
                    Text("Payer ${"%.2f".format(totalPrice)} €")
                }
            }
        }
    }
}
