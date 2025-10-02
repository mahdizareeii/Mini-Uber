package com.shared.miniuber.feature.home

import androidx.lifecycle.viewModelScope
import com.shared.miniuber.component.map.MapState.MarkerData
import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.core.base.UiState
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.TripResponse
import com.shared.miniuber.feature.home.HomeState.ButtonState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.destination
import miniuber.composeapp.generated.resources.drivers_count
import miniuber.composeapp.generated.resources.drop_off
import miniuber.composeapp.generated.resources.empty
import miniuber.composeapp.generated.resources.pickup
import miniuber.composeapp.generated.resources.pickup_location
import miniuber.composeapp.generated.resources.search_driver
import miniuber.composeapp.generated.resources.starting_location
import org.jetbrains.compose.resources.getString

class HomePresenter(
    private val interactor: HomeContract.Interactor,
    private val router: BaseContract.Router
) : HomeContract.Presenter() {

    private var homeState = HomeState()
        set(value) {
            field = value
            _state.update { UiState.Success(value) }
        }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Init -> init()
            is HomeEvent.NavigateBack -> router.navigateUp()
            is HomeEvent.OnMapStateUpdated -> {
                homeState.mapState.cameraPosition = event.latLng
                getNearbyDrivers()
            }

            is HomeEvent.CancelTravel -> homeState = homeState.copy(
                mapState = homeState.mapState.copy(startPoint = null, endPoint = null),
            )

            is HomeEvent.OnConfirmButtonClicked -> {
                when {
                    homeState.mapState.startPoint == null -> onPickupLocationSelected()
                    homeState.mapState.endPoint == null -> onDropOffLocationSelected()
                    else -> navigateToRideRequest()
                }
            }

            is HomeEvent.OnCancelButtonClicked -> {
                when {
                    homeState.mapState.endPoint != null -> changeStateToDropOffLocation()
                    homeState.mapState.startPoint != null -> changeStateToPickupLocation()
                }
            }
        }
    }

    private fun getNearbyDrivers() {
        _state.update { UiState.Loading }
        viewModelScope.launch {
            interactor.getNearbyDrivers(
                LocationRequest(
                    lat = homeState.mapState.cameraPosition.latitude,
                    lng = homeState.mapState.cameraPosition.longitude
                )
            ).onSuccess { result ->
                homeState = homeState.copy(
                    driversCountState = homeState.driversCountState.copy(
                        text = getString(Res.string.drivers_count, result.size)
                    )
                )
            }.onFailure { error ->
                _state.update { UiState.Error(error = error.message) }
            }
        }
    }

    private fun init() {
        _state.update { UiState.Loading }
        viewModelScope.launch {
            interactor.getLastTrip().onSuccess {
                when (val state = it.state) {
                    is TripResponse.TripState.EndTrip -> {
                        homeState = homeState
                    }

                    is TripResponse.TripState.NoTrip -> {
                        homeState = homeState
                    }

                    is TripResponse.TripState.OnTrip -> {
                        homeState = homeState
                    }

                    is TripResponse.TripState.NavigateToRideRequest -> {
                        homeState = homeState
                        router.navigate<AppScreens.RideRequestScreen>(
                            route = AppScreens.RideRequestScreen(),
                            popUpTo = AppScreens.HomeScreen::class,
                            popUpToInclusive = true
                        )
                    }
                }
            }.onFailure { error ->
                _state.update { UiState.Error(error = error.message) }
            }
        }
    }

    private fun onPickupLocationSelected() {
        viewModelScope.launch {
            homeState = homeState.copy(
                mapState = homeState.mapState.copy(
                    startPoint = MarkerData(
                        latLng = homeState.mapState.cameraPosition,
                        title = getString(Res.string.pickup),
                        snipped = getString(Res.string.starting_location)
                    )
                ),
                confirmButtonState = homeState.confirmButtonState.copy(
                    text = Res.string.drop_off
                ),
                cancelButtonState = ButtonState(text = Res.string.empty)
            )
        }
    }

    private fun onDropOffLocationSelected() {
        viewModelScope.launch {
            homeState = homeState.copy(
                mapState = homeState.mapState.copy(
                    endPoint = MarkerData(
                        latLng = homeState.mapState.cameraPosition,
                        title = getString(Res.string.drop_off),
                        snipped = getString(Res.string.destination)
                    )
                ),
                confirmButtonState = homeState.confirmButtonState.copy(
                    text = Res.string.search_driver
                ),
                markerState = homeState.markerState.copy(visible = false)
            )
        }
    }

    private fun navigateToRideRequest() {
        if (homeState.mapState.startPoint == null) return
        if (homeState.mapState.endPoint == null) return

        router.navigate(
            route = AppScreens.RideRequestScreen(
                pickupLat = homeState.mapState.startPoint!!.latLng.latitude,
                pickupLng = homeState.mapState.startPoint!!.latLng.longitude,
                dropOffLat = homeState.mapState.endPoint!!.latLng.latitude,
                dropOffLng = homeState.mapState.endPoint!!.latLng.longitude
            ),
            popUpTo = AppScreens.HomeScreen::class,
            popUpToInclusive = true
        )
    }

    private fun changeStateToDropOffLocation() {
        homeState = homeState.copy(
            mapState = homeState.mapState.copy(endPoint = null),
            confirmButtonState = homeState.confirmButtonState.copy(
                text = Res.string.drop_off
            ),
            markerState = homeState.markerState.copy(visible = true)
        )
    }

    private fun changeStateToPickupLocation() {
        homeState = homeState.copy(
            mapState = homeState.mapState.copy(startPoint = null),
            confirmButtonState = homeState.confirmButtonState.copy(
                text = Res.string.pickup_location
            ),
            cancelButtonState = null,
            markerState = homeState.markerState.copy(visible = true)
        )
    }
}