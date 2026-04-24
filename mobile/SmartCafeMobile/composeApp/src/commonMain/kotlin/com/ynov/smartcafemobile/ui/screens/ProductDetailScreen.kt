package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.model.Product
import com.ynov.smartcafemobile.ui.theme.CafeOrange

import com.ynov.smartcafemobile.ui.components.categoryColor
import com.ynov.smartcafemobile.ui.components.categoryEmoji

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    onAddToCart: (Product, Int) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "${"%.2f".format(product.price * quantity)} €",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = CafeOrange
                        )
                    }
                    Button(
                        onClick = { onAddToCart(product, quantity) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CafeOrange),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("🛒  Ajouter au panier", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(categoryColor(product.categoryName)),
                contentAlignment = Alignment.Center
            ) {
                Text(categoryEmoji(product.categoryName), fontSize = 80.sp)
            }

            Column(modifier = Modifier.padding(20.dp)) {
                // Badge catégorie
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CafeOrange.copy(alpha = 0.15f)
                ) {
                    Text(
                        product.categoryName,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = CafeOrange,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(12.dp))

                Text(
                    product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${"%.2f".format(product.price)} €",
                    style = MaterialTheme.typography.headlineSmall,
                    color = CafeOrange,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text("Description", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(
                    product.description ?: "Produit frais de qualité, préparé avec soin par notre équipe.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(Modifier.height(16.dp))

                // Stock
                val stockColor = if (product.stock > 5) MaterialTheme.colorScheme.primary
                                 else MaterialTheme.colorScheme.error
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = stockColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        if (product.stock > 0) "✓ En stock (${product.stock} restants)"
                        else "✗ Rupture de stock",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = stockColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Sélecteur quantité
                Text("Quantité", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilledIconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text("−", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        "$quantity",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.widthIn(min = 32.dp),
                    )
                    FilledIconButton(
                        onClick = { if (quantity < product.stock.coerceAtLeast(10)) quantity++ },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = CafeOrange)
                    ) {
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = androidx.compose.ui.graphics.Color.White)
                    }
                }
            }
        }
    }
}
