package com.shared.miniuber.component.map

data class MapState(
    val startPoint: LatLng? = null,
    val endPoint: LatLng? = null
) {
    data class LatLng(
        val latitude: Double,
        val longitude: Double
    )
}

//@Composable
//fun rememberMapState(map: KmpMap): MapState { return remember { MapState(map) } }