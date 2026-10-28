package com.shared.miniuber.component.adaptive

import kotlin.math.max
import kotlin.math.min

/**
 * Uses measured content size rather than screen size. Regions arrive in root-window dp;
 * origin accounts for the host's safe areas and any navigation chrome above this content.
 * Bounds remain physical coordinates: RTL swaps the side-by-side panes, not the hinge.
 */
fun adaptivePaneLayout(
    width: Float,
    height: Float,
    regions: List<AdaptiveRegion> = emptyList(),
    originX: Float = 0f,
    originY: Float = 0f,
    isRtl: Boolean = false,
    hasSelection: Boolean = false
): AdaptivePaneLayout {
    val content = AdaptivePaneBounds(
        left = 0f,
        top = 0f,
        right = width.coerceAtLeast(0f),
        bottom = height.coerceAtLeast(0f)
    )
    val localRegions = regions.mapNotNull { region ->
        if (!listOf(region.left, region.top, region.right, region.bottom).all { it.isFinite() } ||
            region.right < region.left || region.bottom < region.top
        ) {
            return@mapNotNull null
        }
        val left = region.left - originX
        val right = region.right - originX
        val top = region.top - originY
        val bottom = region.bottom - originY
        if (right < 0f || bottom < 0f || left > content.right || top > content.bottom) {
            return@mapNotNull null
        }
        region.copy(
            left = left.coerceIn(0f, content.right),
            right = right.coerceIn(0f, content.right),
            top = top.coerceIn(0f, content.bottom),
            bottom = bottom.coerceIn(0f, content.bottom)
        )
    }
    // A spanning obstruction also separates panes, even when the platform reports only occlusion.
    val divider = localRegions.filter { region ->
        val vertical = region.bottom - region.top >= region.right - region.left
        if (vertical) {
            region.top <= 0f && region.bottom >= content.bottom && region.left > 0f && region.right < content.right
        } else {
            region.left <= 0f && region.right >= content.right && region.top > 0f && region.bottom < content.bottom
        }
    }.maxByOrNull { max(it.bottom - it.top, it.right - it.left) }

    val initial = if (divider != null) {
        if (divider.bottom - divider.top >= divider.right - divider.left) {
            val left = AdaptivePaneBounds(left = 0f, top = 0f, right = divider.left, bottom = content.bottom)
            val right = AdaptivePaneBounds(left = divider.right, top = 0f, right = content.right, bottom = content.bottom)
            AdaptivePaneLayout(
                mode = AdaptiveLayoutMode.SIDE_BY_SIDE,
                list = if (isRtl) right else left,
                detail = if (isRtl) left else right
            )
        } else {
            AdaptivePaneLayout(
                mode = AdaptiveLayoutMode.TABLETOP,
                list = AdaptivePaneBounds(left = 0f, top = 0f, right = content.right, bottom = divider.top),
                detail = AdaptivePaneBounds(left = 0f, top = divider.bottom, right = content.right, bottom = content.bottom)
            )
        }
    } else if (content.width >= 600f) {
        val listWidth = (content.width * 0.35f).coerceIn(240f, 360f)
        if (isRtl) {
            AdaptivePaneLayout(
                mode = AdaptiveLayoutMode.SIDE_BY_SIDE,
                list = AdaptivePaneBounds(
                    left = content.right - listWidth,
                    top = 0f,
                    right = content.right,
                    bottom = content.bottom
                ),
                detail = AdaptivePaneBounds(
                    left = 0f,
                    top = 0f,
                    right = content.right - listWidth - 24f,
                    bottom = content.bottom
                )
            )
        } else {
            AdaptivePaneLayout(
                mode = AdaptiveLayoutMode.SIDE_BY_SIDE,
                list = AdaptivePaneBounds(left = 0f, top = 0f, right = listWidth, bottom = content.bottom),
                detail = AdaptivePaneBounds(
                    left = listWidth + 24f,
                    top = 0f,
                    right = content.right,
                    bottom = content.bottom
                )
            )
        }
    } else if (hasSelection) {
        AdaptivePaneLayout(mode = AdaptiveLayoutMode.COMPACT_DETAIL, list = null, detail = content)
    } else {
        AdaptivePaneLayout(mode = AdaptiveLayoutMode.COMPACT_LIST, list = content, detail = null)
    }

    val occlusions = localRegions.filter { it.kind == AdaptiveRegionKind.OCCLUSION }
    val unobstructed = initial.copy(
        list = initial.list?.let { excludeOcclusions(bounds = it, occlusions = occlusions) },
        detail = initial.detail?.let { excludeOcclusions(bounds = it, occlusions = occlusions) },
    )
    if (unobstructed.isTwoPane && (unobstructed.list?.area == 0f || unobstructed.detail?.area == 0f)) {
        // Keep both destinations reachable if one side of the device becomes unavailable.
        // Reuse a surviving pane so this fallback never spans a physical division.
        val remaining = listOfNotNull(unobstructed.list, unobstructed.detail).maxBy { it.area }
        return if (hasSelection) {
            AdaptivePaneLayout(mode = AdaptiveLayoutMode.COMPACT_DETAIL, list = null, detail = remaining)
        } else {
            AdaptivePaneLayout(mode = AdaptiveLayoutMode.COMPACT_LIST, list = remaining, detail = null)
        }
    }
    return unobstructed
}

/** Retain a usable rectangle so neither drawing nor touch targets can straddle an occlusion. */
private fun excludeOcclusions(
    bounds: AdaptivePaneBounds,
    occlusions: List<AdaptiveRegion>
): AdaptivePaneBounds {
    return occlusions.fold(bounds) { available, region ->
        val left = max(available.left, region.left)
        val top = max(available.top, region.top)
        val right = min(available.right, region.right)
        val bottom = min(available.bottom, region.bottom)
        if (right <= left || bottom <= top) {
            available
        } else {
            listOf(
                available.copy(right = left),
                available.copy(left = right),
                available.copy(bottom = top),
                available.copy(top = bottom)
            ).maxBy { it.area }
        }
    }
}
