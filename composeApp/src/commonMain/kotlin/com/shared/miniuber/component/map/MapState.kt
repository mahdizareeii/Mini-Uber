package com.shared.miniuber.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class MapState(map: KmpMap) {



}

@Composable
fun rememberMapState(map: KmpMap): MapState { return remember { MapState(map) } }