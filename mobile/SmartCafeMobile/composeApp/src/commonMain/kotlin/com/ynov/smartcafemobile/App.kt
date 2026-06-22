package com.ynov.smartcafemobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.ynov.smartcafemobile.navigation.AppScreen
import com.ynov.smartcafemobile.network.httpClient
import com.ynov.smartcafemobile.ui.screens.*
import com.ynov.smartcafemobile.ui.theme.Beige
import com.ynov.smartcafemobile.ui.theme.DarkGreen
import com.ynov.smartcafemobile.ui.theme.Gold
import com.ynov.smartcafemobile.ui.theme.SmartCafeTheme
import com.ynov.smartcafemobile.viewmodel.AuthViewModel
import com.ynov.smartcafemobile.viewmodel.CartViewModel
import com.ynov.smartcafemobile.viewmodel.OrderViewModel
import com.ynov.smartcafemobile.viewmodel.ProductViewModel

private enum class BottomTab(val icon: ImageVector, val label: String) {
    Home(Icons.Default.Home, "Accueil"),
    Cart(Icons.Default.ShoppingCart, "Panier"),
    Account(Icons.Default.Person, "Mon compte")
}

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory(httpClient)) }
            .build()
    }

    SmartCafeTheme {
        val navStack = remember { mutableStateListOf<AppScreen>(AppScreen.Login) }
        val authViewModel = remember { AuthViewModel() }
        val productViewModel = remember { ProductViewModel() }
        val cartViewModel = remember { CartViewModel() }
        val orderViewModel = remember { OrderViewModel() }

        val currentUser by authViewModel.currentUser.collectAsState()
        val cartItemCount by cartViewModel.totalItems.collectAsState()

        fun navigate(screen: AppScreen) = navStack.add(screen)
        fun goBack() { if (navStack.size > 1) navStack.removeLast() }

        val currentScreen = navStack.last()

        // Show auth screens without bottom bar
        val isAuthScreen = currentScreen is AppScreen.Login || currentScreen is AppScreen.Register

        if (isAuthScreen) {
            when (currentScreen) {
                is AppScreen.Login -> LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navStack.clear()
                        navStack.add(AppScreen.Home)
                    },
                    onNavigateToRegister = { navigate(AppScreen.Register) }
                )
                is AppScreen.Register -> RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                        navStack.clear()
                        navStack.add(AppScreen.Home)
                    },
                    onBack = ::goBack
                )
                else -> {}
            }
        } else {
            // Main app with bottom navigation
            val selectedTab = when (currentScreen) {
                is AppScreen.Account, is AppScreen.Profile,
                is AppScreen.Orders, is AppScreen.Offers,
                is AppScreen.Help, is AppScreen.Legal -> BottomTab.Account
                is AppScreen.Cart, is AppScreen.OrderType, is AppScreen.QRScan,
                is AppScreen.Payment, is AppScreen.Confirmation -> BottomTab.Cart
                else -> BottomTab.Home
            }

            Scaffold(
                containerColor = Beige,
                bottomBar = {
                    SmartCafeBottomBar(
                        selectedTab = selectedTab,
                        cartItemCount = cartItemCount,
                        onTabSelected = { tab ->
                            when (tab) {
                                BottomTab.Home -> {
                                    navStack.clear()
                                    navStack.add(AppScreen.Home)
                                }
                                BottomTab.Cart -> {
                                    navStack.clear()
                                    navStack.add(AppScreen.Home)
                                    navStack.add(AppScreen.Cart)
                                }
                                BottomTab.Account -> {
                                    navStack.clear()
                                    navStack.add(AppScreen.Home)
                                    navStack.add(AppScreen.Account)
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (val screen = navStack.last()) {
                        is AppScreen.Home -> HomeScreen(
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            onCartClick = { navigate(AppScreen.Cart) }
                        )
                        is AppScreen.Cart -> CartScreen(
                            cartViewModel = cartViewModel,
                            onCheckout = { navigate(AppScreen.OrderType) },
                            onBack = ::goBack
                        )
                        is AppScreen.OrderType -> OrderTypeScreen(
                            onClickAndCollect = { navigate(AppScreen.Payment("CLICK_AND_COLLECT", null)) },
                            onOnSite = { navigate(AppScreen.QRScan) },
                            onBack = ::goBack
                        )
                        is AppScreen.QRScan -> QRScanScreen(
                            onTableScanned = { tableId -> navigate(AppScreen.Payment("ON_SITE", tableId)) },
                            onBack = ::goBack
                        )
                        is AppScreen.Payment -> PaymentScreen(
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel,
                            currentUser = currentUser,
                            orderType = screen.orderType,
                            tableId = screen.tableId,
                            onPaymentSuccess = { orderId ->
                                val homeIndex = navStack.indexOfFirst { it is AppScreen.Home }
                                if (homeIndex >= 0) {
                                    val count = navStack.size - homeIndex - 1
                                    repeat(count) { navStack.removeLast() }
                                } else {
                                    navStack.clear()
                                    navStack.add(AppScreen.Home)
                                }
                                navStack.add(AppScreen.Confirmation(orderId, screen.orderType, screen.tableId))
                            },
                            onBack = ::goBack
                        )
                        is AppScreen.Confirmation -> ConfirmationScreen(
                            orderId = screen.orderId,
                            orderType = screen.orderType,
                            tableId = screen.tableId,
                            onBackToHome = {
                                navStack.clear()
                                navStack.add(AppScreen.Home)
                            }
                        )
                        is AppScreen.Account -> AccountScreen(
                            authViewModel = authViewModel,
                            onProfileClick = { navigate(AppScreen.Profile) },
                            onOrdersClick = { navigate(AppScreen.Orders) },
                            onMenuClick = {
                                navStack.clear()
                                navStack.add(AppScreen.Home)
                            },
                            onOffersClick = { navigate(AppScreen.Offers) },
                            onHelpClick = { navigate(AppScreen.Help) },
                            onLegalClick = { navigate(AppScreen.Legal) },
                            onLogout = {
                                authViewModel.logout()
                                navStack.clear()
                                navStack.add(AppScreen.Login)
                            }
                        )
                        is AppScreen.Profile -> ProfileScreen(
                            authViewModel = authViewModel,
                            onBack = ::goBack
                        )
                        is AppScreen.Orders -> OrdersScreen(onBack = ::goBack)
                        is AppScreen.Offers -> OffersScreen(onBack = ::goBack)
                        is AppScreen.Help -> HelpScreen(onBack = ::goBack)
                        is AppScreen.Legal -> LegalScreen(onBack = ::goBack)
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartCafeBottomBar(
    selectedTab: BottomTab,
    cartItemCount: Int,
    onTabSelected: (BottomTab) -> Unit
) {
    Surface(
        color = DarkGreen,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                BadgedBox(
                    badge = {
                        if (tab == BottomTab.Cart && cartItemCount > 0) {
                            Badge(containerColor = Color.White) {
                                Text("$cartItemCount", color = DarkGreen)
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Gold else Color.White.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onTabSelected(tab) },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                tab.icon,
                                contentDescription = tab.label,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
