package com.shared.miniuber

import androidx.compose.ui.window.ComposeUIViewController
import com.shared.miniuber.component.map.MapState
import platform.UIKit.UIViewController

fun MainViewController(
    mapUiViewController: (
        state: MapState,
        onCameraChanged: (latLng: MapState.LatLng) -> Unit
    ) -> UIViewController
) = ComposeUIViewController {
    mapViewController = mapUiViewController
    App()
}

lateinit var mapViewController: (
    state: MapState,
    onCameraChanged: (latLng: MapState.LatLng) -> Unit
) -> UIViewController