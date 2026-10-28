package com.shared.miniuber.feature.adaptive

sealed interface AdaptiveDemoEvent {
    data class SelectRide(val id: String) : AdaptiveDemoEvent
    data object BackToList : AdaptiveDemoEvent
    data object NavigateBack : AdaptiveDemoEvent
}
