package com.shared.miniuber

import androidx.compose.ui.window.ComposeUIViewController
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.shared.miniuber.component.adaptive.AdaptiveWindowState
import com.shared.miniuber.component.adaptive.LocalAdaptiveWindowState
import com.shared.miniuber.component.map.MapState
import platform.UIKit.UIViewController

fun MainViewController(
    mapsConfigured: Boolean,
    adaptiveWindowState: AdaptiveWindowState,
    mapUiViewController: (
        state: MapState,
        onCameraChanged: (latLng: MapState.LatLng) -> Unit
    ) -> UIViewController
) = ComposeUIViewController {
    CompositionLocalProvider(
        LocalMapViewControllerFactory provides mapUiViewController,
        LocalAdaptiveWindowState provides adaptiveWindowState,
    ) {
        App(mapsConfigured = mapsConfigured)
    }
}

internal val LocalMapViewControllerFactory = staticCompositionLocalOf<
    (MapState, (MapState.LatLng) -> Unit) -> UIViewController
> { error("An iOS map controller factory must be supplied by MainViewController") }
