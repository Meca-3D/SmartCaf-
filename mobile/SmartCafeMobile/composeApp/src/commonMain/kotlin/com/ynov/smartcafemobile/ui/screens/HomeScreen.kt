package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.layout.ContentScale
import com.ynov.smartcafemobile.ui.components.ProductCard
import com.ynov.smartcafemobile.ui.theme.Beige
import org.jetbrains.compose.resources.painterResource
import smartcafemobile.composeapp.generated.resources.Res
import smartcafemobile.composeapp.generated.resources.banniere_de_pub
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
    val cartItems by cartViewModel.items.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = if (searchQuery.isBlank()) products else products.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }
    val nouveautes = filteredProducts
    val carteProducts = filteredProducts

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
                            Row(
                                    modifier = Modifier
                                        .horizontalScroll(rememberScrollState())
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                FilterChip(
                                    selected = selectedCategory == null,
                                    onClick = { productViewModel.filterByCategory(null) },
                                    label = { Text("Tout") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Gold,
                                        selectedLabelColor = Color.White
                                    )
                                )
                                categories.forEach { cat ->
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
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(nouveautes) { product ->
                                    val qty = cartItems.find { it.product.id == product.id }?.quantity ?: 0
                                    Box(modifier = Modifier.width(240.dp)) {
                                        ProductCard(
                                            product = product,
                                            onAddToCart = { cartViewModel.addItem(product) },
                                            cartQuantity = qty,
                                            onRemoveFromCart = { cartViewModel.decreaseItem(product.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Bannière publicitaire
                    item {
                        Spacer(Modifier.height(20.dp))
                        Image(
                            painter = painterResource(Res.drawable.banniere_de_pub),
                            contentDescription = "Bannière publicitaire",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(Modifier.height(4.dp))
                    }

                    // Section La Carte
                    item {
                        Spacer(Modifier.height(20.dp))
                        HomeSectionTitle("La Carte")
                    }
                    if (carteProducts.isEmpty() && !isLoading) {
                        item {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                repeat(3) {
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
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(carteProducts) { product ->
                                    val qty = cartItems.find { it.product.id == product.id }?.quantity ?: 0
                                    Box(modifier = Modifier.width(180.dp)) {
                                        ProductCard(
                                            product = product,
                                            onAddToCart = { cartViewModel.addItem(product) },
                                            cartQuantity = qty,
                                            onRemoveFromCart = { cartViewModel.decreaseItem(product.id) }
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
