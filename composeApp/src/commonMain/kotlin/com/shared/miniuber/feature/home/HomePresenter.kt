package com.shared.miniuber.feature.home

import androidx.lifecycle.viewModelScope
import com.shared.miniuber.component.map.MapState.MarkerData
import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.TripResponse
import com.shared.miniuber.feature.home.HomeState.ButtonState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.destination
import miniuber.composeapp.generated.resources.drop_off
import miniuber.composeapp.generated.resources.empty
import miniuber.composeapp.generated.resources.pickup
import miniuber.composeapp.generated.resources.pickup_location
import miniuber.composeapp.generated.resources.search_driver
import miniuber.composeapp.generated.resources.starting_location
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

class HomePresenter(
    private val interactor: HomeContract.Interactor,
    private val router: BaseContract.Router,
    private val resolveString: suspend (StringResource) -> String = { getString(it) }
) : HomeContract.Presenter() {

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Init -> init()
            is HomeEvent.NavigateBack -> router.navigateUp()
            is HomeEvent.OnMapStateUpdated -> {
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            mapState = it.data.mapState.copy(cameraPosition = event.latLng)
                        ),
                        isLoading = false,
                        error = null
                    )
                }
                getNearbyDrivers()
            }

            is HomeEvent.CancelTravel -> _state.update {
                it.copy(
                    data = it.data.copy(
                        mapState = it.data.mapState.copy(startPoint = null, endPoint = null)
                    ),
                    isLoading = false,
                    error = null
                )
            }

            is HomeEvent.OnConfirmButtonClicked -> {
                val mapState = _state.value.data.mapState
                when {
                    mapState.startPoint == null -> onPickupLocationSelected()
                    mapState.endPoint == null -> onDropOffLocationSelected()
                    else -> navigateToRideRequest()
                }
            }

            is HomeEvent.OnCancelButtonClicked -> {
                val mapState = _state.value.data.mapState
                when {
                    mapState.endPoint != null -> changeStateToDropOffLocation()
                    mapState.startPoint != null -> changeStateToPickupLocation()
                }
            }
        }
    }

    private fun getNearbyDrivers() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            val cameraPosition = _state.value.data.mapState.cameraPosition
            interactor.getNearbyDrivers(
                LocationRequest(
                    lat = cameraPosition.latitude,
                    lng = cameraPosition.longitude
                )
            ).onSuccess { result ->
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            driversCountState = it.data.driversCountState.copy(count = result.size)
                        ),
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun init() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            interactor.getLastTrip().onSuccess { trip ->
                _state.update { it.copy(isLoading = false, error = null) }
                when (trip.state) {
                    is TripResponse.TripState.EndTrip,
                    is TripResponse.TripState.NoTrip,
                    is TripResponse.TripState.OnTrip -> Unit

                    is TripResponse.TripState.NavigateToRideRequest -> {
                        router.navigate<AppScreens.RideRequestScreen>(
                            route = AppScreens.RideRequestScreen(),
                            popUpTo = AppScreens.HomeScreen::class,
                            popUpToInclusive = true
                        )
                    }
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun onPickupLocationSelected() {
        viewModelScope.launch {
            val cameraPosition = _state.value.data.mapState.cameraPosition
            val title = resolveString(Res.string.pickup)
            val snipped = resolveString(Res.string.starting_location)
            _state.update {
                it.copy(
                    data = it.data.copy(
                        mapState = it.data.mapState.copy(
                            startPoint = MarkerData(
                                latLng = cameraPosition,
                                title = title,
                                snipped = snipped
                            )
                        ),
                        confirmButtonState = it.data.confirmButtonState.copy(
                            text = Res.string.drop_off
                        ),
                        cancelButtonState = ButtonState(text = Res.string.empty)
                    ),
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private fun onDropOffLocationSelected() {
        viewModelScope.launch {
            val cameraPosition = _state.value.data.mapState.cameraPosition
            val title = resolveString(Res.string.drop_off)
            val snipped = resolveString(Res.string.destination)
            _state.update {
                it.copy(
                    data = it.data.copy(
                        mapState = it.data.mapState.copy(
                            endPoint = MarkerData(
                                latLng = cameraPosition,
                                title = title,
                                snipped = snipped
                            )
                        ),
                        confirmButtonState = it.data.confirmButtonState.copy(
                            text = Res.string.search_driver
                        ),
                        markerState = it.data.markerState.copy(visible = false)
                    ),
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private fun navigateToRideRequest() {
        val mapState = _state.value.data.mapState
        val pickup = mapState.startPoint ?: return
        val dropOff = mapState.endPoint ?: return

        router.navigate(
            route = AppScreens.RideRequestScreen(
                pickupLat = pickup.latLng.latitude,
                pickupLng = pickup.latLng.longitude,
                dropOffLat = dropOff.latLng.latitude,
                dropOffLng = dropOff.latLng.longitude
            ),
            popUpTo = AppScreens.HomeScreen::class,
            popUpToInclusive = true
        )
    }

    private fun changeStateToDropOffLocation() {
        _state.update {
            it.copy(
                data = it.data.copy(
                    mapState = it.data.mapState.copy(endPoint = null),
                    confirmButtonState = it.data.confirmButtonState.copy(
                        text = Res.string.drop_off
                    ),
                    markerState = it.data.markerState.copy(visible = true)
                ),
                isLoading = false,
                error = null
            )
        }
    }

    private fun changeStateToPickupLocation() {
        _state.update {
            it.copy(
                data = it.data.copy(
                    mapState = it.data.mapState.copy(startPoint = null),
                    confirmButtonState = it.data.confirmButtonState.copy(
                        text = Res.string.pickup_location
                    ),
                    cancelButtonState = null,
                    markerState = it.data.markerState.copy(visible = true)
                ),
                isLoading = false,
                error = null
            )
        }
    }
}
