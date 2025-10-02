package com.shared.miniuber.core

import kotlinx.serialization.Serializable

sealed class AppScreens {
    @Serializable
    data object HomeScreen : AppScreens()

    @Serializable
    data class RideRequestScreen(
        val pickupLat: Double,
        val pickupLng: Double,
        val dropOffLat: Double,
        val dropOffLng: Double,
    ) : AppScreens() {
        constructor() : this(Double.NaN, Double.NaN, Double.NaN, Double.NaN)

        fun isValidLocation() = (pickupLat + pickupLng + dropOffLat + dropOffLng) > Double.NaN
    }
}