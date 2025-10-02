package com.shared.miniuber.feature.home

import com.shared.miniuber.component.map.MapState
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.pickup_location
import org.jetbrains.compose.resources.StringResource

data class HomeState(
    val mapState: MapState = MapState(),
    val markerState: MarkerState = MarkerState(visible = true),
    val driversCountState: DriversCountState = DriversCountState(text = "..."),
    val confirmButtonState: ButtonState = ButtonState(text = Res.string.pickup_location),
    val cancelButtonState: ButtonState? = null,
) {
    data class MarkerState(val visible: Boolean)
    data class ButtonState(val text: StringResource)
    data class DriversCountState(val text: String)
}