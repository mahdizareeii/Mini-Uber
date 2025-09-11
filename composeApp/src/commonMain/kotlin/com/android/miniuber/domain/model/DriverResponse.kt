package com.android.miniuber.domain.model

data class DriverResponse(
    val id: String,
    val name: String,
    val vehicle: String,
    val location: LocationResponse,
    val etaMinutes: Int
)