package com.shared.miniuber.feature.adaptive

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.shared.miniuber.component.adaptive.AdaptiveLayoutMode
import com.shared.miniuber.component.adaptive.AdaptivePanesComponent
import com.shared.miniuber.component.adaptive.PlatformBackHandler
import com.shared.miniuber.component.adaptive.adaptivePaneLayout
import com.shared.miniuber.component.adaptive.currentAdaptiveRegions
import com.shared.miniuber.feature.adaptive.component.DemoColors
import com.shared.miniuber.feature.adaptive.component.RideDetailComponent
import com.shared.miniuber.feature.adaptive.component.RideListComponent
import com.shared.miniuber.feature.adaptive.component.RideScrollStatesSaver
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdaptiveDemoScreen(
    modifier: Modifier = Modifier,
    presenter: AdaptiveDemoContract.Presenter = koinViewModel()
) {
    val uiState by presenter.state.collectAsState()
    val adaptiveState = uiState.data
    val regions = currentAdaptiveRegions()
    val density = LocalDensity.current
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    var origin by remember { mutableStateOf(Offset.Zero) }
    // Keep scroll state outside the panes so folding and list/detail switches retain position.
    val listScrollState = rememberLazyListState()
    val detailScrollStates = rememberSaveable(saver = RideScrollStatesSaver) {
        mutableMapOf<String, ScrollState>()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(color = DemoColors.Background)
            .windowInsetsPadding(insets = WindowInsets.safeDrawing)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInWindow()
                origin = Offset(
                    x = position.x / density.density,
                    y = position.y / density.density
                )
            }
    ) {
        val layout = adaptivePaneLayout(
            width = maxWidth.value,
            height = maxHeight.value,
            regions = regions,
            originX = origin.x,
            originY = origin.y,
            isRtl = isRtl,
            hasSelection = adaptiveState.selectedRideId != null
        )
        val selectedRide = adaptiveState.effectiveSelectedRide(isTwoPane = layout.isTwoPane)
        PlatformBackHandler(enabled = layout.mode == AdaptiveLayoutMode.COMPACT_DETAIL) {
            presenter.onEvent(event = AdaptiveDemoEvent.BackToList)
        }
        AdaptivePanesComponent(
            layout = layout,
            list = {
                RideListComponent(
                    rides = adaptiveState.rides,
                    selectedId = selectedRide?.id,
                    scrollState = listScrollState,
                    mode = layout.mode,
                    onSelect = { presenter.onEvent(event = AdaptiveDemoEvent.SelectRide(id = it)) },
                    onNavigateHome = { presenter.onEvent(event = AdaptiveDemoEvent.NavigateBack) }
                )
            },
            detail = {
                selectedRide?.let { ride ->
                    RideDetailComponent(
                        ride = ride,
                        scrollState = detailScrollStates.getOrPut(key = ride.id) { ScrollState(initial = 0) },
                        isCompact = !layout.isTwoPane,
                        onBackToList = { presenter.onEvent(event = AdaptiveDemoEvent.BackToList) },
                        onNavigateHome = { presenter.onEvent(event = AdaptiveDemoEvent.NavigateBack) }
                    )
                }
            }
        )
    }
}
