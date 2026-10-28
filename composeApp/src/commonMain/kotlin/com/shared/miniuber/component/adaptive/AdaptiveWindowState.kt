package com.shared.miniuber.component.adaptive

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** The native host updates this state when its reserved regions or window geometry changes. */
class AdaptiveWindowState {
    var regions: List<AdaptiveRegion> by mutableStateOf(emptyList())
        private set

    fun updateRegions(regions: List<AdaptiveRegion>) {
        this.regions = regions.filter { region ->
            region.left.isFinite() && region.top.isFinite() &&
                region.right.isFinite() && region.bottom.isFinite() &&
                region.right >= region.left && region.bottom >= region.top
        }.distinct().toList()
    }
}
