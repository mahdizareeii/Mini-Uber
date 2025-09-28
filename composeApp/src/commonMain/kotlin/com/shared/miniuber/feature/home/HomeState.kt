package com.shared.miniuber.feature.home

import com.shared.miniuber.component.map.MapState
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.pickup_location
import org.jetbrains.compose.resources.StringResource

data class HomeState(
    val cameraPosition: MapState.LatLng? = null,
    val mapState: MapState = MapState(),
    val confirmButtonState: ButtonState = ButtonState(text = Res.string.pickup_location)
) {
    data class ButtonState(val text: StringResource)
}