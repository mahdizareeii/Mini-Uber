package com.shared.miniuber.feature.riderequest

sealed interface RideRequestEvent {
    data object Init: RideRequestEvent
    data object NavigateBack : RideRequestEvent
    data object CancelRideRequest: RideRequestEvent
}