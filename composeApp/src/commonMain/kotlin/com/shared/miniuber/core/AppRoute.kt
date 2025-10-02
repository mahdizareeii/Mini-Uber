package com.shared.miniuber.core

sealed class AppRoute(
    val route: String,
    val deepLink: String
) {
    data object Home : AppRoute(
        route = "home",
        deepLink = "home"
    )

    data object SearchDriver : AppRoute(
        route = "search_driver",
        deepLink = "search_driver"
    )
}