package com.shared.miniuber

import androidx.compose.ui.window.ComposeUIViewController
import com.shared.miniuber.component.map.MapState
import platform.UIKit.UIViewController

fun MainViewController(
    mapUiViewController: (
        onCameraChanged: (latLng: MapState.LatLng) -> Unit
    ) -> UIViewController
) = ComposeUIViewController {
    mapViewController = mapUiViewController
    App()
}

lateinit var mapViewController: (
    onCameraChanged: (latLng: MapState.LatLng) -> Unit
) -> UIViewController