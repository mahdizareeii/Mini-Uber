package com.shared.miniuber.feature.adaptive

import com.shared.miniuber.feature.adaptive.data.DemoRides
import com.shared.miniuber.feature.adaptive.model.DemoRide

data class AdaptiveDemoState(
    val rides: List<DemoRide> = DemoRides.rides,
    val selectedRideId: String? = null
) {
    fun effectiveSelectedRide(isTwoPane: Boolean): DemoRide? {
        val selectedRide = rides.firstOrNull { it.id == selectedRideId }
        return selectedRide ?: if (isTwoPane) rides.firstOrNull() else null
    }
}
