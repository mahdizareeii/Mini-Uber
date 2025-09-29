package com.shared.miniuber.component.map

data class MapState(
    var cameraPosition: LatLng = LatLng(35.6997, 51.3380),
    val cameraZoom: Float = 15f,
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