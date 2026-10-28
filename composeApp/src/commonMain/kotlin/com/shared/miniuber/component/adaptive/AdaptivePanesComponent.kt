package com.shared.miniuber.component.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Constraints

@Composable
fun AdaptivePanesComponent(
    layout: AdaptivePaneLayout,
    list: @Composable () -> Unit,
    detail: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val panes = listOfNotNull(layout.list, layout.detail)
    Layout(
        modifier = modifier.fillMaxSize(),
        content = {
            if (layout.list != null) {
                Box(modifier = Modifier.clipToBounds().testTag(tag = "adaptive-list")) {
                    list()
                }
            }
            if (layout.detail != null) {
                Box(modifier = Modifier.clipToBounds().testTag(tag = "adaptive-detail")) {
                    detail()
                }
            }
        }
    ) { measurables, constraints ->
        // Round inward so drawing and touch targets never cross fractional native bounds.
        val placements = panes.map { bounds -> bounds.toPixels(density = density) }
        val placeables = measurables.mapIndexed { index, measurable ->
            val bounds = placements[index]
            measurable.measure(
                constraints = Constraints.fixed(width = bounds.width, height = bounds.height)
            )
        }
        layout(width = constraints.maxWidth, height = constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val bounds = placements[index]
                placeable.place(x = bounds.left, y = bounds.top)
            }
        }
    }
}
