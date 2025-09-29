package com.shared.miniuber.feature.home

import androidx.lifecycle.viewModelScope
import com.shared.miniuber.component.map.MapState.MarkerData
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.core.base.UiState
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.feature.home.HomeState.ButtonState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.close_symbol
import miniuber.composeapp.generated.resources.destination
import miniuber.composeapp.generated.resources.drivers_count
import miniuber.composeapp.generated.resources.drop_off
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
        viewModelScope.launch {
            when (event) {
                is HomeEvent.Init -> init()
                is HomeEvent.NavigateBack -> router.navigateBack()
                is HomeEvent.OnMapStateUpdated -> {
                    homeState.mapState.cameraPosition = event.latLng
                    getNearbyDrivers()
                }

                is HomeEvent.CancelTravel -> homeState = homeState.copy(
                    mapState = homeState.mapState.copy(startPoint = null, endPoint = null),
                )

                is HomeEvent.OnConfirmButtonClicked -> {
                    when {
                        homeState.mapState.startPoint == null -> {
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
                                cancelButtonState = ButtonState(text = Res.string.close_symbol)
                            )
                        }

                        homeState.mapState.endPoint == null -> {
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
                                )
                            )
                        }
                    }
                }

                is HomeEvent.OnCancelButtonClicked -> {
                    when {
                        homeState.mapState.endPoint != null -> {
                            homeState = homeState.copy(
                                mapState = homeState.mapState.copy(endPoint = null),
                                confirmButtonState = homeState.confirmButtonState.copy(
                                    text = Res.string.drop_off
                                )
                            )
                        }

                        homeState.mapState.startPoint != null -> {
                            homeState = homeState.copy(
                                mapState = homeState.mapState.copy(startPoint = null),
                                confirmButtonState = homeState.confirmButtonState.copy(
                                    text = Res.string.pickup_location
                                ),
                                cancelButtonState = null
                            )
                        }
                    }
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
        //TODO call trip api
    }
}