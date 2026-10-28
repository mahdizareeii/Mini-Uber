package com.shared.miniuber.component.adaptive

import kotlin.math.ceil
import kotlin.math.floor

data class AdaptivePaneBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    val width: Float get() = (right - left).coerceAtLeast(minimumValue = 0f)
    val height: Float get() = (bottom - top).coerceAtLeast(minimumValue = 0f)
    val area: Float get() = width * height

    internal fun toPixels(density: Float) = PanePixels(
        left = ceil(x = left * density).toInt(),
        top = ceil(x = top * density).toInt(),
        right = floor(x = right * density).toInt(),
        bottom = floor(x = bottom * density).toInt()
    )
}
