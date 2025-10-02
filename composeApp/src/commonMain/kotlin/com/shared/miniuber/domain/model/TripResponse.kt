package com.shared.miniuber.domain.model

data class TripResponse(
    val state: TripState
) {
    sealed class TripState {
        data object NoTrip : TripState()
        data object LookingForDriver : TripState()
        data class OnTrip(val ride: RideResponse) : TripState()
        data class EndTrip(val ride: RideResponse) : TripState()
    }
}