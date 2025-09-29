package com.shared.miniuber.component.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.delay
import kotlin.math.pow

@Composable
actual fun GoogleMapComponent(
    modifier: Modifier,
    state: MapState,
    onCameraChanged: (latLng: MapState.LatLng) -> Unit,
) {
    val startMarkerState = rememberUpdatedMarkerState()
    val endMarkerState = rememberUpdatedMarkerState()
    val cameraPositionState = rememberCameraPositionState() {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                state.cameraPosition.latitude,
                state.cameraPosition.longitude
            ),
            state.cameraZoom
        )
    }

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
                update = CameraUpdateFactory.newLatLngZoom(startPoint, state.cameraZoom)
            )
        } else if (state.endPoint != null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(endPoint, state.cameraZoom)
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        if (state.startPoint != null && state.endPoint != null) {
            AnimatedCurveLine(
                start = LatLng(
                    state.startPoint.latLng.latitude,
                    state.startPoint.latLng.longitude,
                ),
                end = LatLng(
                    state.endPoint.latLng.latitude,
                    state.endPoint.latLng.longitude,
                )
            )
        }
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


fun generateCurvePoints(
    start: LatLng,
    end: LatLng,
    curveHeight: Double = 0.1,
    numPoints: Int = 100
): List<LatLng> {
    val points = mutableListOf<LatLng>()

    val midLat = (start.latitude + end.latitude) / 2
    val midLng = (start.longitude + end.longitude) / 2

    val dx = end.longitude - start.longitude
    val dy = end.latitude - start.latitude

    val controlLat = midLat + (-dx * curveHeight)
    val controlLng = midLng + (dy * curveHeight)

    for (i in 0..numPoints) {
        val t = i.toDouble() / numPoints
        val lat = (1 - t).pow(2) * start.latitude +
                2 * (1 - t) * t * controlLat +
                t.pow(2) * end.latitude
        val lng = (1 - t).pow(2) * start.longitude +
                2 * (1 - t) * t * controlLng +
                t.pow(2) * end.longitude
        points.add(LatLng(lat, lng))
    }
    return points
}

@Composable
fun AnimatedCurveLine(start: LatLng, end: LatLng) {
    val allPoints = remember(start, end) {
        generateCurvePoints(
            start = start,
            end = end,
            curveHeight = 0.2
        )
    }
    var visiblePointsCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(allPoints) {
        for (i in 1..allPoints.size) {
            visiblePointsCount = i
            delay(10)
        }
    }

    if (visiblePointsCount > 1) {
        Polyline(
            points = allPoints.take(visiblePointsCount),
            color = MaterialTheme.colorScheme.primary,
            width = 10f,
            jointType = JointType.ROUND,
        )
    }
}

