package com.shared.miniuber.core

import kotlinx.serialization.Serializable

sealed class AppScreens {
    @Serializable
    data object HomeScreen : AppScreens()

    @Serializable
    data class RideRequestScreen(
        val pickupLat: Double? = null,
        val pickupLng: Double? = null,
        val dropOffLat: Double? = null,
        val dropOffLng: Double? = null,
    ) : AppScreens()
}