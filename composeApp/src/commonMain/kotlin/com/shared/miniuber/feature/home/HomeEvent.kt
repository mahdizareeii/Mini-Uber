package com.shared.miniuber.feature.home

import com.shared.miniuber.domain.model.RideRequest

sealed interface HomeEvent {
    data object NavigateBack : HomeEvent
    data object LoadDrivers : HomeEvent
    data class RequestRide(val request: RideRequest) : HomeEvent
}