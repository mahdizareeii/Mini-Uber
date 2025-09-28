package com.shared.miniuber.component.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
actual fun GoogleMapComponent(
    modifier: Modifier,
    state: MapState,
    onCameraChanged: (latLng: MapState.LatLng) -> Unit,
) {
    val startMarkerState = rememberUpdatedMarkerState()
    val endMarkerState = rememberUpdatedMarkerState()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(cameraPositionState.position) {
        onCameraChanged(
            MapState.LatLng(
                latitude = cameraPositionState.position.target.latitude,
                longitude = cameraPositionState.position.target.longitude
            )
        )
    }

    val startPoint = LatLng(
        /* latitude = */ state.startPoint?.latLng?.latitude ?: 0.0,
        /* longitude = */ state.startPoint?.latLng?.longitude ?: 0.0
    )
    val endPoint = LatLng(
        /* latitude = */ state.endPoint?.latLng?.latitude ?: 0.0,
        /* longitude = */ state.endPoint?.latLng?.longitude ?: 0.0
    )

    startMarkerState.position = startPoint
    endMarkerState.position = endPoint

    LaunchedEffect(startPoint, endPoint) {
        if (state.startPoint != null && state.endPoint != null) {
            val bounds = LatLngBounds.builder()
                .include(startPoint)
                .include(endPoint)
                .build()

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        } else if (state.startPoint != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(startPoint, 10f)
            )
        } else if (state.endPoint != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(endPoint, 10f)
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        if (state.startPoint != null) {
            Marker(
                state = startMarkerState,
                title = state.startPoint.title,
                snippet = state.startPoint.snipped,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            )
        }

        if (state.endPoint != null) {
            Marker(
                state = endMarkerState,
                title = state.endPoint.title,
                snippet = state.endPoint.snipped,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
        }
    }
}