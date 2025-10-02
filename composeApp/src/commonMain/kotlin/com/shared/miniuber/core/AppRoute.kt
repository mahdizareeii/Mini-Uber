package com.shared.miniuber.core

sealed class AppRoute(
    val route: String,
    val deepLink: String
) {
    data object Home : AppRoute(
        route = "home",
        deepLink = "home"
    )

    data object RideRequest : AppRoute(
        route = "ride_request",
        deepLink = "ride_request"
    )
}