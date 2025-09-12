package com.shared.miniuber.domain.model

data class RideRequest(
    val pickup: LocationResponse,
    val dropOff: LocationResponse
)