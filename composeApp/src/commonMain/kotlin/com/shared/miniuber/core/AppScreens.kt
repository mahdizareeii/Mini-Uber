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
        var requestMode: RequestMode = RequestMode.RegisterNewRequest

        constructor() : this(0.0, 0.0, 0.0, 0.0) {
            requestMode = RequestMode.GetPreviousRequest
        }

        enum class RequestMode {
            GetPreviousRequest,
            RegisterNewRequest
        }
    }
}