package com.shared.miniuber.domain.model

data class RideRequest(
    val pickup: LocationRequest,
    val dropOff: LocationRequest
)