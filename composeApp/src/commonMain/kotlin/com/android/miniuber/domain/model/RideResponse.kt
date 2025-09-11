package com.android.miniuber.domain.model

import com.android.miniuber.util.getPlatform

data class RideResponse(
    val id: String,
    val pickup: LocationResponse,
    val dropOff: LocationResponse,
    val requestedAt: String = getPlatform().generateUuid()
)