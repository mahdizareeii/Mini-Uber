package com.shared.miniuber.component.adaptive

/** An active platform region, measured in dp from the root window's physical top-left. */
data class AdaptiveRegion(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val kind: AdaptiveRegionKind
)
