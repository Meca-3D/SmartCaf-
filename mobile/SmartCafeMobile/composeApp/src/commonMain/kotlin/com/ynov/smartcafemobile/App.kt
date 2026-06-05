package com.ynov.smartcafemobile

import androidx.compose.runtime.*
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.ynov.smartcafemobile.navigation.AppScreen
import com.ynov.smartcafemobile.network.httpClient
import com.ynov.smartcafemobile.ui.screens.*
import com.ynov.smartcafemobile.ui.theme.SmartCafeTheme
import com.ynov.smartcafemobile.viewmodel.AuthViewModel
import com.ynov.smartcafemobile.viewmodel.CartViewModel
import com.ynov.smartcafemobile.viewmodel.OrderViewModel
import com.ynov.smartcafemobile.viewmodel.ProductViewModel

@Composable
fun App() {
    // Configure Coil3 image loader with Ktor network fetcher
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

        fun navigate(screen: AppScreen) = navStack.add(screen)
        fun goBack() { if (navStack.size > 1) navStack.removeLast() }

        when (val screen = navStack.last()) {
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
                    // Keep only Home in the stack, then add Confirmation
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
        }
    }
}
