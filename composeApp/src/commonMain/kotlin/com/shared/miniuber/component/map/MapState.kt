package com.shared.miniuber.component.map

data class MapState(
    val startPoint: MarkerData? = null,
    val endPoint: MarkerData? = null
) {
    data class MarkerData(
        val latLng: LatLng,
        val title: String,
        val snipped: String
    )

    data class LatLng(
        val latitude: Double,
        val longitude: Double
    )
}

//@Composable
//fun rememberMapState(map: KmpMap): MapState { return remember { MapState(map) } }