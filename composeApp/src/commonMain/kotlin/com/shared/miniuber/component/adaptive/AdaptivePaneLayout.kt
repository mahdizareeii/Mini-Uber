package com.shared.miniuber.component.adaptive

data class AdaptivePaneLayout(
    val mode: AdaptiveLayoutMode,
    val list: AdaptivePaneBounds?,
    val detail: AdaptivePaneBounds?
) {
    val isTwoPane: Boolean
        get() = mode == AdaptiveLayoutMode.SIDE_BY_SIDE || mode == AdaptiveLayoutMode.TABLETOP
}
