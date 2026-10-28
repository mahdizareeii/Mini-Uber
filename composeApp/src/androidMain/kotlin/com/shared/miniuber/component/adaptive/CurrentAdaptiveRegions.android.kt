package com.shared.miniuber.component.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity

@Composable
actual fun currentAdaptiveRegions(): List<AdaptiveRegion> {
    val hinges = currentWindowAdaptiveInfoV2().windowPosture.hingeList
    val density = LocalDensity.current.density
    return remember(hinges, density) {
        hinges.mapNotNull { hinge ->
            if (hinge.isFlat && !hinge.isSeparating && !hinge.isOccluding) {
                return@mapNotNull null
            }
            // Adaptive reports bounds in window pixels; shared layout uses window dp.
            AdaptiveRegion(
                left = hinge.bounds.left / density,
                top = hinge.bounds.top / density,
                right = hinge.bounds.right / density,
                bottom = hinge.bounds.bottom / density,
                kind = if (hinge.isOccluding) {
                    AdaptiveRegionKind.OCCLUSION
                } else {
                    AdaptiveRegionKind.DIVISION
                },
            )
        }
    }
}

