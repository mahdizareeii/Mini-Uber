package com.shared.miniuber.feature.landing

sealed interface LandingEvent {
    data object OnMapDemoClicked : LandingEvent
    data object OnAdaptiveDemoClicked : LandingEvent
}
