package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.model.Order
import com.ynov.smartcafemobile.ui.theme.Beige
import com.ynov.smartcafemobile.ui.theme.BrandRed
import com.ynov.smartcafemobile.ui.theme.BrandText
import com.ynov.smartcafemobile.ui.theme.DarkGreen
import com.ynov.smartcafemobile.ui.theme.Gold
import com.ynov.smartcafemobile.viewmodel.OrderViewModel

private val STATUS_LABELS = mapOf(
    "PENDING" to "En attente",
    "IN_PROGRESS" to "En cours",
    "COMPLETED" to "Complétée",
    "CANCELLED" to "Annulée"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    orderViewModel: OrderViewModel,
    onBack: () -> Unit
) {
    val orders by orderViewModel.orders.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Smart Café", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Gold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.padding(end = 12.dp).size(36.dp).clip(CircleShape).background(DarkGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkGreen)
            )
        },
        containerColor = Beige
    ) { paddingValues ->
        if (orders.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Mes commandes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandText,
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Gold.copy(alpha = 0.3f))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Star, contentDescription = null, tint = Gold.copy(alpha = 0.4f), modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(16.dp))
                Text(
                    "Vous n'avez pas encore de commandes.",
                    color = BrandText.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )
                Spacer(Modifier.weight(1f))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "Mes commandes",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Gold.copy(alpha = 0.3f)
                    )
                }
                items(orders.sortedByDescending { it.id }) { order ->
                    OrderCard(order)
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order) {
    val statusLabel = STATUS_LABELS[order.status] ?: order.status
    val statusColor = when (order.status) {
        "PENDING"     -> Color(0xFFD97706)
        "IN_PROGRESS" -> Color(0xFF2563EB)
        "COMPLETED"   -> Color(0xFF16A34A)
        "CANCELLED"   -> BrandRed
        else          -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Commande #${order.id}",
                    fontWeight = FontWeight.Bold,
                    color = BrandText,
                    fontSize = 16.sp
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        statusLabel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = statusColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
            if (order.items.isNotEmpty()) {
                Text(
                    order.items.joinToString(", ") { it.product?.name ?: "Produit" },
                    color = BrandText.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${order.items.size} article${if (order.items.size > 1) "s" else ""}",
                    color = BrandText.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
                Text(
                    "%.2f €".format(order.totalPrice),
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    fontSize = 15.sp
                )
            }
        }
    }
}
