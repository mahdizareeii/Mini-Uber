package com.shared.miniuber.feature.home

import com.shared.miniuber.component.map.MapState

sealed interface HomeEvent {
    data class OnMapStateUpdated(val latLng: MapState.LatLng) : HomeEvent
    data object Init: HomeEvent
    data object OnConfirmButtonClicked : HomeEvent
    data object CancelTravel : HomeEvent
    data object NavigateBack : HomeEvent
}