package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ynov.smartcafemobile.ui.components.ProductCard
import com.ynov.smartcafemobile.ui.theme.Beige
import com.ynov.smartcafemobile.ui.theme.DarkGreen
import com.ynov.smartcafemobile.ui.theme.Gold
import com.ynov.smartcafemobile.viewmodel.CartViewModel
import com.ynov.smartcafemobile.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onCartClick: () -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val error by productViewModel.error.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val cartItemCount by cartViewModel.totalItems.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = if (searchQuery.isBlank()) products else products.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    val nouveautes = filteredProducts.take(2)
    val carteProducts = filteredProducts.drop(2).take(6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Smart Café",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Gold
                    )
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(containerColor = Gold) {
                                    Text("$cartItemCount", color = Color.White)
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DarkGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = onCartClick, modifier = Modifier.size(36.dp)) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Panier",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen
                )
            )
        },
        containerColor = Beige
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Gold)
                }
            }
            error != null -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                        Button(
                            onClick = { productViewModel.loadProducts() },
                            colors = ButtonDefaults.buttonColors(containerColor = Gold)
                        ) {
                            Text("Réessayer")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Rechercher un produit...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Gold) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Gold,
                                unfocusedBorderColor = Gold.copy(alpha = 0.4f),
                                cursorColor = Gold
                            ),
                            singleLine = true
                        )
                    }

                    // Category filter chips
                    val categories = productViewModel.getCategories()
                    if (categories.isNotEmpty()) {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    FilterChip(
                                        selected = selectedCategory == null,
                                        onClick = { productViewModel.filterByCategory(null) },
                                        label = { Text("Tout") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Gold,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                                items(categories) { cat ->
                                    FilterChip(
                                        selected = selectedCategory == cat,
                                        onClick = { productViewModel.filterByCategory(cat) },
                                        label = { Text(cat) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Gold,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Section Nouveautés
                    item {
                        HomeSectionTitle("Nouveautés")
                    }
                    if (nouveautes.isEmpty()) {
                        item {
                            Row(
                                Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                repeat(2) {
                                    Box(
                                        Modifier
                                            .weight(1f)
                                            .height(120.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFEDD99A))
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(nouveautes) { product ->
                                    Box(modifier = Modifier.width(180.dp)) {
                                        ProductCard(
                                            product = product,
                                            onAddToCart = { cartViewModel.addItem(product) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section Restaurants
                    item {
                        Spacer(Modifier.height(20.dp))
                        HomeSectionTitle("Restaurants")
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { productViewModel.filterByCategory(null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Gold)
                            ) {
                                Text("Recherche", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { productViewModel.filterByCategory(null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Gold)
                            ) {
                                Text("Résultats", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Section La Carte
                    item {
                        Spacer(Modifier.height(20.dp))
                        HomeSectionTitle("La Carte")
                    }
                    if (carteProducts.isEmpty() && !isLoading) {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(3) {
                                    Box(
                                        Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFEDD99A))
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(carteProducts) { product ->
                                    Box(modifier = Modifier.width(140.dp)) {
                                        ProductCard(
                                            product = product,
                                            onAddToCart = { cartViewModel.addItem(product) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
