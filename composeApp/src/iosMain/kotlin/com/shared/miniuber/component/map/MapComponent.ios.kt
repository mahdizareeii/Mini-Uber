package com.shared.miniuber.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.shared.miniuber.LocalMapViewControllerFactory

@Composable
actual fun GoogleMapComponent(
    modifier: Modifier,
    state: MapState,
    onCameraChanged: (latLng: MapState.LatLng) -> Unit
) {
    val mapViewController = LocalMapViewControllerFactory.current
    val currentOnCameraChanged by rememberUpdatedState(onCameraChanged)
    UIKitViewController(
        modifier = modifier,
        factory = {
            mapViewController.invoke(state) { currentOnCameraChanged(it) }
        },
        update = { controller ->
            if (controller is KmpMap) { controller.updateState(state) }
        }
    )
}
