package com.shared.miniuber.component.adaptive

internal data class PanePixels(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    val width: Int get() = (right - left).coerceAtLeast(minimumValue = 0)
    val height: Int get() = (bottom - top).coerceAtLeast(minimumValue = 0)
}
