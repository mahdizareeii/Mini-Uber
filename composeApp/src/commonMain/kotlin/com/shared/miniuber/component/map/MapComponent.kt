package com.shared.miniuber.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GoogleMapComponent(
    modifier: Modifier = Modifier,
    state: MapState,
    onCameraChanged: (latLng: MapState.LatLng)-> Unit = {}
)