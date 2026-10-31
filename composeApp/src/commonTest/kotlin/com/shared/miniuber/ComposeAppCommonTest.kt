package com.shared.miniuber

import com.shared.miniuber.component.map.MapState
import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.data.repository.FakeDriverRepositoryImpl
import com.shared.miniuber.domain.model.DriverResponse
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.LocationResponse
import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.domain.model.RideResponse
import com.shared.miniuber.domain.model.TripResponse
import com.shared.miniuber.feature.home.HomeContract
import com.shared.miniuber.feature.home.HomeEvent
import com.shared.miniuber.feature.home.HomePresenter
import com.shared.miniuber.feature.landing.LandingEvent
import com.shared.miniuber.feature.landing.LandingPresenter
import com.shared.miniuber.feature.riderequest.RideRequestContract
import com.shared.miniuber.feature.riderequest.RideRequestEvent
import com.shared.miniuber.feature.riderequest.RideRequestPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ComposeAppCommonTest {
    @Test
    fun landingOpensTheMapDestination() = presenterTest {
        val router = FakeRouter()
        val presenter = LandingPresenter(router = router)

        presenter.onEvent(event = LandingEvent.OnMapDemoClicked)

        assertEquals(listOf<Any>(AppScreens.HomeScreen), router.routes)
    }

    @Test
    fun landingOpensTheAdaptiveDestination() = presenterTest {
        val router = FakeRouter()
        val presenter = LandingPresenter(router = router)

        presenter.onEvent(event = LandingEvent.OnAdaptiveDemoClicked)

        assertEquals(listOf<Any>(AppScreens.AdaptiveDemoScreen), router.routes)
    }

    @Test
    fun actionSubscribersReceiveEmissionsFromOneStableStream() = presenterTest {
        val presenter = ActionPresenter()
        val received = mutableListOf<String>()
        assertSame(presenter.action, presenter.action)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            presenter.action.take(2).toList(received)
        }

        presenter.publish(value = "first")
        presenter.publish(value = "second")

        assertEquals(listOf("first", "second"), received)
    }

    @Test
    fun fakeRepositoryKeepsTheOriginalStatelessTripBehavior() = runTest {
        val repository = FakeDriverRepositoryImpl(dispatcher = StandardTestDispatcher(testScheduler))
        val request = RideRequest(
            pickup = LocationRequest(lat = 35.0, lng = 51.0),
            dropOff = LocationRequest(lat = 35.1, lng = 51.1)
        )

        val created = repository.requestRide(request = request).getOrThrow()
        val lastRide = repository.getLastRideRequest().getOrThrow()

        assertEquals(request.pickup.lat, created.pickup.lat)
        assertEquals(request.dropOff.lng, created.dropOff.lng)
        assertTrue(created.requestedAt.isNotEmpty())
        assertTrue(lastRide.pickup.lat.isNaN())
        assertTrue(lastRide.dropOff.lng.isNaN())
        assertIs<TripResponse.TripState.NoTrip>(repository.getLastTrip().getOrThrow().state)
    }

    @Test
    fun rideInitializationUsesTheOriginalArgumentFallback() = presenterTest {
        val interactor = FakeRideInteractor()
        val presenter = RideRequestPresenter(interactor = interactor, router = FakeRouter(argument = validRoute))

        presenter.onEvent(event = RideRequestEvent.Init)
        assertTrue(presenter.state.value.isLoading)
        advanceUntilIdle()

        // The original NaN comparison falls back to getLastRideRequest, even for these coordinates.
        assertEquals(1, interactor.restoreCalls)
        assertTrue(interactor.requests.isEmpty())
        assertEquals(exampleRide.requestedAt, presenter.state.value.data.requestedAt)
        assertFalse(presenter.state.value.isLoading)
        assertNull(presenter.state.value.error)
    }

    @Test
    fun presenterKeepsItsArgumentsWhenTheCurrentRouteChanges() = presenterTest {
        val interactor = FakeRideInteractor()
        val router = FakeRouter(argument = validRoute)
        val presenter = RideRequestPresenter(interactor = interactor, router = router)
        router.argument = AppScreens.LandingScreen

        presenter.onEvent(event = RideRequestEvent.Init)
        advanceUntilIdle()

        assertEquals(1, interactor.restoreCalls)
        assertEquals(1, router.argumentReads)
    }

    @Test
    fun cancellingOnlyNavigatesBackToMapSelection() = presenterTest {
        val interactor = FakeRideInteractor()
        val router = FakeRouter()
        val presenter = RideRequestPresenter(interactor = interactor, router = router)

        presenter.onEvent(event = RideRequestEvent.CancelRideRequest)
        advanceUntilIdle()

        assertEquals(listOf<Any>(AppScreens.HomeScreen), router.routes)
        assertTrue(interactor.requests.isEmpty())
        assertEquals(0, interactor.restoreCalls)
    }

    @Test
    fun rideBackUsesRouterNavigationUp() = presenterTest {
        val router = FakeRouter()
        val presenter = RideRequestPresenter(interactor = FakeRideInteractor(), router = router)

        presenter.onEvent(event = RideRequestEvent.NavigateBack)

        assertEquals(1, router.navigateUpCalls)
        assertTrue(router.routes.isEmpty())
    }

    @Test
    fun homeExitIsSeparateFromRideCancellation() = presenterTest {
        val router = FakeRouter()
        val interactor = FakeRideInteractor()
        val presenter = RideRequestPresenter(interactor = interactor, router = router)

        presenter.onEvent(event = RideRequestEvent.NavigateHome)

        assertEquals(listOf<Any>(AppScreens.LandingScreen), router.routes)
        assertTrue(interactor.requests.isEmpty())
        assertEquals(0, interactor.restoreCalls)
    }

    @Test
    fun rideFailureIsRepresentedWithoutReplacingContent() = presenterTest {
        val interactor = FakeRideInteractor()
        val presenter = RideRequestPresenter(interactor = interactor, router = FakeRouter())
        presenter.onEvent(event = RideRequestEvent.Init)
        advanceUntilIdle()
        val content = presenter.state.value.data
        interactor.restoreResult = Result.failure(IllegalStateException("Offline"))

        presenter.onEvent(event = RideRequestEvent.Init)
        advanceUntilIdle()

        assertEquals(content, presenter.state.value.data)
        assertEquals("Offline", presenter.state.value.error)
        assertFalse(presenter.state.value.isLoading)
    }

    @Test
    fun cameraChangesPreserveImmutableSnapshotsAndFetchDrivers() = presenterTest {
        val interactor = FakeHomeInteractor()
        val presenter = HomePresenter(interactor = interactor, router = FakeRouter()) { "label" }
        presenter.onEvent(event = HomeEvent.Init)
        advanceUntilIdle()
        assertFalse(presenter.state.value.isLoading)
        val original = presenter.state.value.data
        assertTrue(interactor.locations.isEmpty())

        presenter.onEvent(event = HomeEvent.OnMapStateUpdated(latLng = MapState.LatLng(36.0, 52.0)))
        advanceUntilIdle()

        assertEquals(MapState.LatLng(35.6997, 51.3380), original.mapState.cameraPosition)
        assertEquals(MapState.LatLng(36.0, 52.0), presenter.state.value.data.mapState.cameraPosition)
        assertEquals(listOf(LocationRequest(lat = 36.0, lng = 52.0)), interactor.locations)
        assertEquals(2, presenter.state.value.data.driversCountState.count)
    }

    @Test
    fun pickupDestinationAndBackSelectionKeepOriginalFlow() = presenterTest {
        val presenter = HomePresenter(interactor = FakeHomeInteractor(), router = FakeRouter()) { "label" }
        presenter.onEvent(event = HomeEvent.Init)
        advanceUntilIdle()
        val original = presenter.state.value.data

        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)
        advanceUntilIdle()
        assertNotNull(presenter.state.value.data.mapState.startPoint)
        val destination = MapState.LatLng(36.0, 52.0)
        presenter.onEvent(event = HomeEvent.OnMapStateUpdated(latLng = destination))
        advanceUntilIdle()
        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)
        advanceUntilIdle()

        assertEquals(destination, presenter.state.value.data.mapState.endPoint?.latLng)
        assertFalse(presenter.state.value.data.markerState.visible)
        assertNull(original.mapState.startPoint)
        presenter.onEvent(event = HomeEvent.OnCancelButtonClicked)
        assertNull(presenter.state.value.data.mapState.endPoint)
        assertNotNull(presenter.state.value.data.mapState.startPoint)
        presenter.onEvent(event = HomeEvent.OnCancelButtonClicked)
        assertNull(presenter.state.value.data.mapState.startPoint)
        assertNull(presenter.state.value.data.cancelButtonState)
        assertTrue(presenter.state.value.data.markerState.visible)
    }

    @Test
    fun homePassesSelectedCoordinatesToTheRideRoute() = presenterTest {
        val router = FakeRouter()
        val presenter = HomePresenter(interactor = FakeHomeInteractor(), router = router) { "label" }
        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)
        advanceUntilIdle()
        presenter.onEvent(event = HomeEvent.OnMapStateUpdated(latLng = MapState.LatLng(36.0, 52.0)))
        advanceUntilIdle()
        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)
        advanceUntilIdle()

        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)

        assertEquals(listOf<Any>(AppScreens.RideRequestScreen(35.6997, 51.3380, 36.0, 52.0)), router.routes)
    }

    @Test
    fun selectingLocationsKeepsThePointChosenBeforeLabelsResolve() = presenterTest {
        val presenter = HomePresenter(interactor = FakeHomeInteractor(), router = FakeRouter()) {
            delay(timeMillis = 50)
            "label"
        }
        val pickup = presenter.state.value.data.mapState.cameraPosition
        val dropOff = MapState.LatLng(36.0, 52.0)
        val laterCameraPosition = MapState.LatLng(37.0, 53.0)

        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)
        runCurrent()
        presenter.onEvent(event = HomeEvent.OnMapStateUpdated(latLng = dropOff))
        advanceUntilIdle()

        assertEquals(pickup, presenter.state.value.data.mapState.startPoint?.latLng)
        assertEquals(dropOff, presenter.state.value.data.mapState.cameraPosition)

        presenter.onEvent(event = HomeEvent.OnConfirmButtonClicked)
        runCurrent()
        presenter.onEvent(event = HomeEvent.OnMapStateUpdated(latLng = laterCameraPosition))
        advanceUntilIdle()

        assertEquals(dropOff, presenter.state.value.data.mapState.endPoint?.latLng)
        assertEquals(laterCameraPosition, presenter.state.value.data.mapState.cameraPosition)
    }

    private fun presenterTest(block: suspend TestScope.() -> Unit) = runTest {
        Dispatchers.setMain(dispatcher = StandardTestDispatcher(testScheduler))
        try {
            block()
        } finally {
            Dispatchers.resetMain()
        }
    }
}

private val validRoute = AppScreens.RideRequestScreen(35.0, 51.0, 35.1, 51.1)
private val exampleRide = RideResponse("ride-1", LocationResponse(35.0, 51.0), LocationResponse(35.1, 51.1), "example-time")

private class ActionPresenter : BaseContract.Presenter<Unit, Unit, String>(Unit) {
    override fun onEvent(event: Unit) = Unit

    suspend fun publish(value: String) {
        _action.emit(value = value)
    }
}

private class FakeHomeInteractor : HomeContract.Interactor {
    val locations = mutableListOf<LocationRequest>()

    override suspend fun getLastTrip() = Result.success(TripResponse(TripResponse.TripState.NoTrip))

    override suspend fun getNearbyDrivers(location: LocationRequest): Result<List<DriverResponse>> {
        locations += location
        return Result.success(List(2) { DriverResponse("$it", "Driver", "Car", LocationResponse(35.0, 51.0), 3) })
    }
}

private class FakeRideInteractor : RideRequestContract.Interactor {
    val requests = mutableListOf<RideRequest>()
    var restoreCalls = 0
    var restoreResult = Result.success(exampleRide)

    override suspend fun rideRequest(request: RideRequest): Result<RideResponse> {
        requests += request
        return Result.success(exampleRide)
    }

    override suspend fun getLastRideRequest(): Result<RideResponse> {
        restoreCalls++
        delay(timeMillis = 50)
        return restoreResult
    }
}

private class FakeRouter(
    var argument: AppScreens = AppScreens.RideRequestScreen()
) : BaseContract.Router {
    val routes = mutableListOf<Any>()
    var argumentReads = 0
    var navigateUpCalls = 0

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getScreenArg(screen: KClass<T>): T {
        argumentReads++
        check(screen.isInstance(argument))
        return argument as T
    }

    override fun <RESULT> setParentResult(key: String, value: RESULT) = Unit
    override fun <RESULT> getResultFlow(key: String, initialValue: RESULT?): StateFlow<RESULT?>? = null
    override fun navigate(route: String) { routes += route }
    override fun navigate(route: String, popUpTo: String, popUpToInclusive: Boolean) { routes += route }
    override fun <T : Any> navigate(route: T) { routes += route }
    override fun <T : Any> navigate(route: T, popUpTo: KClass<*>, popUpToInclusive: Boolean) { routes += route }
    override fun navigateUp() { navigateUpCalls++ }
}
