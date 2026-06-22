package com.ynov.smartcafemobile.navigation

sealed class AppScreen {
    object Login        : AppScreen()
    object Register     : AppScreen()
    object Home         : AppScreen()
    object Cart         : AppScreen()
    object OrderType    : AppScreen()
    object QRScan       : AppScreen()
    object Account      : AppScreen()
    object Profile      : AppScreen()
    object Orders       : AppScreen()
    object Offers       : AppScreen()
    object Help         : AppScreen()
    object Legal        : AppScreen()
    data class Payment(val orderType: String, val tableId: Long?)                                         : AppScreen()
    data class PaymentSuccess(val orderId: Long, val orderType: String, val tableId: Long?)               : AppScreen()
    data class Confirmation(val orderId: Long, val orderType: String, val tableId: Long?)                 : AppScreen()
}
