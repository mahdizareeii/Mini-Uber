package com.android.miniuber.feature.home

import com.android.miniuber.domain.model.RideRequest

sealed interface HomeEvent {
    data object NavigateBack : HomeEvent
    data object LoadDrivers : HomeEvent
    data class RequestRide(val request: RideRequest) : HomeEvent
}