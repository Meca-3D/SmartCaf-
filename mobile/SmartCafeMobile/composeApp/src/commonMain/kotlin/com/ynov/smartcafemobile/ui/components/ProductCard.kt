package com.ynov.smartcafemobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.model.Product
import com.ynov.smartcafemobile.ui.theme.CafeOrange

fun categoryEmoji(category: String): String = when (category.lowercase()) {
    "sandwichs" -> "🥪"
    "boissons"  -> "☕"
    "salades"   -> "🥗"
    "desserts"  -> "🍰"
    "snacks"    -> "🍟"
    else        -> "🍽️"
}

fun categoryColor(category: String) = when (category.lowercase()) {
    "sandwichs" -> androidx.compose.ui.graphics.Color(0xFFFFE0C4)
    "boissons"  -> androidx.compose.ui.graphics.Color(0xFFB2EBF2)
    "salades"   -> androidx.compose.ui.graphics.Color(0xFFC8E6C9)
    "desserts"  -> androidx.compose.ui.graphics.Color(0xFFF8BBD0)
    "snacks"    -> androidx.compose.ui.graphics.Color(0xFFFFF9C4)
    else        -> androidx.compose.ui.graphics.Color(0xFFE0E0E0)
}

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Image placeholder colorée avec emoji catégorie
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(categoryColor(product.categoryName)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryEmoji(product.categoryName),
                    fontSize = 44.sp
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = product.categoryName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${"%.2f".format(product.price)} €",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CafeOrange
                    )
                    FilledIconButton(
                        onClick = onAddToCart,
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = CafeOrange
                        )
                    ) {
                        Text("+", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
