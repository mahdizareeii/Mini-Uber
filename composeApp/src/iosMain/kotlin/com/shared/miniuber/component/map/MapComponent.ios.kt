package com.shared.miniuber.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.shared.miniuber.mapViewController

@Composable
actual fun GoogleMapComponent(
    modifier: Modifier,
    state: MapState,
    onCameraChanged: (latLng: MapState.LatLng) -> Unit
) {
    UIKitViewController(
        modifier = modifier,
        factory = {
            mapViewController.invoke(state, onCameraChanged)
        },
        update = { controller ->
            if (controller is KmpMap) { controller.updateState(state) }
        }
    )
}