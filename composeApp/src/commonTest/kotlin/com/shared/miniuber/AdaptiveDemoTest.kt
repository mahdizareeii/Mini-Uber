package com.shared.miniuber

import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass
import com.shared.miniuber.component.adaptive.AdaptiveLayoutMode
import com.shared.miniuber.component.adaptive.AdaptiveRegion
import com.shared.miniuber.component.adaptive.AdaptiveRegionKind
import com.shared.miniuber.component.adaptive.AdaptiveWindowState
import com.shared.miniuber.component.adaptive.adaptivePaneLayout
import com.shared.miniuber.feature.adaptive.AdaptiveDemoEvent
import com.shared.miniuber.feature.adaptive.AdaptiveDemoPresenter
import com.shared.miniuber.feature.adaptive.AdaptiveDemoState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AdaptiveDemoTest {
    @Test
    fun compactShowsOnlyListUntilARideIsSelected() {
        val list = adaptivePaneLayout(599f, 800f)
        assertEquals(AdaptiveLayoutMode.COMPACT_LIST, list.mode)
        assertNotNull(list.list)
        assertNull(list.detail)
        val detail = adaptivePaneLayout(599f, 800f, hasSelection = true)
        assertEquals(AdaptiveLayoutMode.COMPACT_DETAIL, detail.mode)
        assertNull(detail.list)
        assertNotNull(detail.detail)
    }

    @Test
    fun sixHundredDpStartsTwoPanesWithMinimumListWidthAndTwentyFourDpGap() {
        val layout = adaptivePaneLayout(600f, 800f)
        assertEquals(AdaptiveLayoutMode.SIDE_BY_SIDE, layout.mode)
        assertEquals(240f, layout.list?.width)
        assertEquals(264f, layout.detail?.left)
        assertEquals(336f, layout.detail?.width)
    }

    @Test
    fun wideListUsesThirtyFivePercentAndStopsAtThreeHundredSixtyDp() {
        assertEquals(280f, adaptivePaneLayout(800f, 800f).list?.width)
        assertEquals(360f, adaptivePaneLayout(1200f, 800f).list?.width)
    }

    @Test
    fun rtlMovesListToPhysicalRightAndRetainsGap() {
        val layout = adaptivePaneLayout(1000f, 700f, isRtl = true)
        assertEquals(650f, layout.list?.left)
        assertEquals(1000f, layout.list?.right)
        assertEquals(0f, layout.detail?.left)
        assertEquals(626f, layout.detail?.right)
    }

    @Test
    fun zeroWidthVerticalFoldOverridesCompactThresholdWithoutCoveringFold() {
        val fold = AdaptiveRegion(250f, 0f, 250f, 700f, AdaptiveRegionKind.DIVISION)
        val layout = adaptivePaneLayout(500f, 700f, listOf(fold))
        assertEquals(AdaptiveLayoutMode.SIDE_BY_SIDE, layout.mode)
        assertEquals(250f, layout.list?.right)
        assertEquals(250f, layout.detail?.left)
        assertEquals(500f, layout.detail?.right)
    }

    @Test
    fun verticalHingeKeepsPhysicalBoundsInRtl() {
        val hinge = AdaptiveRegion(250f, 0f, 270f, 700f, AdaptiveRegionKind.OCCLUSION)
        val layout = adaptivePaneLayout(520f, 700f, listOf(hinge), isRtl = true)
        assertEquals(AdaptiveLayoutMode.SIDE_BY_SIDE, layout.mode)
        assertEquals(270f, layout.list?.left)
        assertEquals(250f, layout.detail?.right)
    }

    @Test
    fun horizontalFoldUsesTopAndBottomEvenInRtl() {
        val fold = AdaptiveRegion(0f, 300f, 500f, 300f, AdaptiveRegionKind.DIVISION)
        val layout = adaptivePaneLayout(500f, 700f, listOf(fold), isRtl = true)
        assertEquals(AdaptiveLayoutMode.TABLETOP, layout.mode)
        assertEquals(300f, layout.list?.bottom)
        assertEquals(300f, layout.detail?.top)
        assertEquals(700f, layout.detail?.bottom)
    }

    @Test
    fun rootCoordinatesAreConvertedAndClippedAfterSafeAreaOffsets() {
        val hinge = AdaptiveRegion(0f, 360f, 550f, 380f, AdaptiveRegionKind.OCCLUSION)
        val layout = adaptivePaneLayout(500f, 700f, listOf(hinge), originX = 20f, originY = 40f)
        assertEquals(AdaptiveLayoutMode.TABLETOP, layout.mode)
        assertEquals(320f, layout.list?.bottom)
        assertEquals(340f, layout.detail?.top)
        assertEquals(500f, layout.detail?.width)
    }

    @Test
    fun offWindowAndInvalidRegionsDoNotChangeTheLayout() {
        val ignored = listOf(
            AdaptiveRegion(800f, 0f, 820f, 900f, AdaptiveRegionKind.OCCLUSION),
            AdaptiveRegion(Float.NaN, 0f, 200f, 900f, AdaptiveRegionKind.DIVISION),
            AdaptiveRegion(220f, 0f, 200f, 900f, AdaptiveRegionKind.DIVISION),
        )
        assertEquals(adaptivePaneLayout(400f, 700f), adaptivePaneLayout(400f, 700f, ignored))
    }

    @Test
    fun partialOcclusionKeepsAllContentAndTouchTargetsInAnUnobstructedRectangle() {
        val occlusion = AdaptiveRegion(0f, 0f, 100f, 100f, AdaptiveRegionKind.OCCLUSION)
        val layout = adaptivePaneLayout(400f, 700f, listOf(occlusion))
        val bounds = assertNotNull(layout.list)
        assertEquals(100f, bounds.top)
        assertEquals(0f, bounds.left)
        assertEquals(400f, bounds.width)
        assertEquals(600f, bounds.height)
    }

    @Test
    fun coveredPaneAndZeroSizeWindowNeverProduceNegativeMeasurements() {
        val covered = adaptivePaneLayout(400f, 700f, listOf(AdaptiveRegion(0f, 0f, 400f, 700f, AdaptiveRegionKind.OCCLUSION)))
        assertEquals(0f, covered.list?.area)
        val empty = adaptivePaneLayout(0f, 0f)
        assertEquals(0f, empty.list?.width)
        assertEquals(0f, empty.list?.height)
    }

    @Test
    fun fullyOccludedListFallsBackToCompactNavigationInTheRemainingPane() {
        val occlusion = AdaptiveRegion(0f, 0f, 400f, 700f, AdaptiveRegionKind.OCCLUSION)
        val list = adaptivePaneLayout(800f, 700f, listOf(occlusion))
        assertEquals(AdaptiveLayoutMode.COMPACT_LIST, list.mode)
        assertFalse(list.isTwoPane)
        assertEquals(400f, list.list?.left)
        assertEquals(800f, list.list?.right)
        assertNull(list.detail)

        val detail = adaptivePaneLayout(800f, 700f, listOf(occlusion), hasSelection = true)
        assertEquals(AdaptiveLayoutMode.COMPACT_DETAIL, detail.mode)
        assertFalse(detail.isTwoPane)
        assertNull(detail.list)
        assertEquals(list.list, detail.detail)
    }

    @Test
    fun fullyOccludedDetailKeepsAnExplicitSelectionReachableInTheListSide() {
        val occlusion = AdaptiveRegion(300f, 0f, 800f, 700f, AdaptiveRegionKind.OCCLUSION)
        val layout = adaptivePaneLayout(800f, 700f, listOf(occlusion), hasSelection = true)
        assertEquals(AdaptiveLayoutMode.COMPACT_DETAIL, layout.mode)
        assertNull(layout.list)
        assertEquals(0f, layout.detail?.left)
        assertEquals(280f, layout.detail?.right)
    }

    @Test
    fun occlusionFallbackRetainsPhysicalCoordinatesInRtlAndTabletop() {
        val rightOcclusion = AdaptiveRegion(400f, 0f, 800f, 700f, AdaptiveRegionKind.OCCLUSION)
        val rtl = adaptivePaneLayout(800f, 700f, listOf(rightOcclusion), isRtl = true)
        assertEquals(AdaptiveLayoutMode.COMPACT_LIST, rtl.mode)
        assertEquals(0f, rtl.list?.left)
        assertEquals(400f, rtl.list?.right)

        val tabletop = adaptivePaneLayout(
            500f, 700f,
            listOf(
                AdaptiveRegion(0f, 300f, 500f, 320f, AdaptiveRegionKind.DIVISION),
                AdaptiveRegion(0f, 0f, 500f, 300f, AdaptiveRegionKind.OCCLUSION),
            ),
        )
        assertEquals(AdaptiveLayoutMode.COMPACT_LIST, tabletop.mode)
        assertEquals(320f, tabletop.list?.top)
        assertEquals(700f, tabletop.list?.bottom)
    }

    @Test
    fun bridgeStateRejectsInvalidBoundsAndKeepsThinDivisions() {
        val state = AdaptiveWindowState()
        val valid = AdaptiveRegion(100f, 0f, 100f, 700f, AdaptiveRegionKind.DIVISION)
        state.updateRegions(listOf(valid, valid, valid.copy(left = Float.POSITIVE_INFINITY), valid.copy(right = 99f)))
        assertEquals(listOf(valid), state.regions)
        state.updateRegions(emptyList())
        assertTrue(state.regions.isEmpty())
    }

    @Test
    fun firstRideIsOnlyAnEffectiveWideSelectionAndDoesNotChangeCompactNavigation() {
        val state = AdaptiveDemoState()
        assertEquals(6, state.rides.size)
        assertEquals(6, state.rides.map { it.id }.distinct().size)
        assertNull(state.selectedRideId)
        assertEquals(state.rides.first(), state.effectiveSelectedRide(isTwoPane = true))
        assertNull(state.effectiveSelectedRide(isTwoPane = false))
        assertNull(state.selectedRideId)
    }

    @Test
    fun explicitSelectionSurvivesModeChangesAndBackOnlyClearsSelection() {
        val presenter = AdaptiveDemoPresenter(router = AdaptiveTestRouter())
        val originalRides = presenter.state.value.data.rides
        val selected = originalRides.last()
        presenter.onEvent(AdaptiveDemoEvent.SelectRide(selected.id))
        repeat(3) {
            assertEquals(selected, presenter.state.value.data.effectiveSelectedRide(isTwoPane = true))
            assertEquals(selected, presenter.state.value.data.effectiveSelectedRide(isTwoPane = false))
        }
        presenter.onEvent(AdaptiveDemoEvent.BackToList)
        assertNull(presenter.state.value.data.selectedRideId)
        assertEquals(originalRides, presenter.state.value.data.rides)
        assertFalse(presenter.state.value.isLoading)
        assertNull(presenter.state.value.error)
    }

    @Test
    fun unknownRideSelectionCannotOpenAnEmptyDetailPane() {
        val presenter = AdaptiveDemoPresenter(router = AdaptiveTestRouter())
        presenter.onEvent(AdaptiveDemoEvent.SelectRide("missing"))
        assertNull(presenter.state.value.data.selectedRideId)
        val valid = presenter.state.value.data.rides[2]
        presenter.onEvent(AdaptiveDemoEvent.SelectRide(valid.id))
        presenter.onEvent(AdaptiveDemoEvent.SelectRide("missing"))
        assertEquals(valid.id, presenter.state.value.data.selectedRideId)
    }
    @Test
    fun returningHomeDoesNotChangeSampleRideSelection() {
        val router = AdaptiveTestRouter()
        val presenter = AdaptiveDemoPresenter(router = router)
        val selected = presenter.state.value.data.rides[1]
        presenter.onEvent(event = AdaptiveDemoEvent.SelectRide(id = selected.id))
        presenter.onEvent(event = AdaptiveDemoEvent.NavigateBack)
        assertEquals(AppScreens.LandingScreen, router.destination)
        assertEquals(AppScreens.LandingScreen::class, router.popUpTo)
        assertTrue(router.popUpToInclusive)
        assertEquals(selected.id, presenter.state.value.data.selectedRideId)
    }

    private class AdaptiveTestRouter : BaseContract.Router {
        var destination: Any? = null
        var popUpTo: KClass<*>? = null
        var popUpToInclusive: Boolean = false

        override fun <T : Any> getScreenArg(screen: KClass<T>): T = error("No screen arguments in the adaptive demo")
        override fun <RESULT> setParentResult(key: String, value: RESULT) = Unit
        override fun <RESULT> getResultFlow(key: String, initialValue: RESULT?): StateFlow<RESULT?>? = null
        override fun navigate(route: String) = Unit
        override fun navigate(route: String, popUpTo: String, popUpToInclusive: Boolean) = Unit
        override fun <T : Any> navigate(route: T) {
            destination = route
        }

        override fun <T : Any> navigate(route: T, popUpTo: KClass<*>, popUpToInclusive: Boolean) {
            destination = route
            this.popUpTo = popUpTo
            this.popUpToInclusive = popUpToInclusive
        }

        override fun navigateUp() = Unit
    }
}
