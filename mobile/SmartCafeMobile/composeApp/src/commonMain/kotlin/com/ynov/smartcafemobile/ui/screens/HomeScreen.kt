package com.ynov.smartcafemobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ynov.smartcafemobile.model.CartItem
import com.ynov.smartcafemobile.model.Category
import com.ynov.smartcafemobile.model.Product
import com.ynov.smartcafemobile.model.User
import com.ynov.smartcafemobile.navigation.Screen
import com.ynov.smartcafemobile.network.ApiService
import com.ynov.smartcafemobile.ui.components.BottomNavBar
import com.ynov.smartcafemobile.ui.components.ProductCard
import com.ynov.smartcafemobile.ui.theme.CafeOrange
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    cart: List<CartItem>,
    onAddToCart: (Product) -> Unit,
    onProductClick: (Product) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val scope = rememberCoroutineScope()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var serverError by remember { mutableStateOf(false) }
    // null = "Tout"
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val api = remember { ApiService() }

    fun loadData() {
        scope.launch {
            isLoading = true
            serverError = false
            val productsResult = async { api.getProducts() }
            val categoriesResult = async { api.getCategories() }
            val p = productsResult.await()
            val c = categoriesResult.await()
            if (p.isFailure) {
                serverError = true
            } else {
                products = p.getOrElse { emptyList() }
                categories = c.getOrElse { emptyList() }
            }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { loadData() }

    val filteredProducts = remember(products, selectedCategory, searchQuery) {
        products.filter { p ->
            val matchesCategory = selectedCategory == null ||
                p.categoryName.equals(selectedCategory!!.name, ignoreCase = true)
            val matchesSearch = searchQuery.isEmpty() ||
                p.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "SmartCafé ☕",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = CafeOrange
                        )
                        Text(
                            "Bonjour, ${user.name.ifBlank { "Client" }} 👋",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cart.isNotEmpty()) Badge { Text("${cart.sumOf { it.quantity }}") }
                        },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        IconButton(onClick = { onNavigate(Screen.Cart) }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Panier", tint = CafeOrange)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Home,
                cartItemCount = cart.sumOf { it.quantity },
                onNavigate = onNavigate
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Barre de recherche
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Rechercher un produit...") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            // Chips catégories (chargées depuis la BDD)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                // Chip "Tout"
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Tout") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CafeOrange,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
                // Une chip par catégorie de la BDD
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory?.id == cat.id,
                        onClick = { selectedCategory = if (selectedCategory?.id == cat.id) null else cat },
                        label = { Text(cat.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CafeOrange,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = CafeOrange)
                        Spacer(Modifier.height(8.dp))
                        Text("Chargement du menu...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else if (serverError) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("⚠️", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Serveur inaccessible",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Vérifiez que le backend est démarré\nsur le port 8081",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = { loadData() },
                            colors = ButtonDefaults.buttonColors(containerColor = CafeOrange)
                        ) {
                            Text("Réessayer")
                        }
                    }
                }
            } else if (filteredProducts.isEmpty() && searchQuery.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("🛒", style = MaterialTheme.typography.displayMedium)
                        Text(
                            "Aucun produit disponible",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "L'admin n'a pas encore ajouté de produits",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (filteredProducts.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucun résultat pour \"$searchQuery\"", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = { onAddToCart(product) },
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }
    }
}
