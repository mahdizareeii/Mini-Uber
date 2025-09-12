package com.shared.miniuber.feature.home

import androidx.lifecycle.viewModelScope
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.core.AppRoute
import com.shared.miniuber.core.base.UiState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            is HomeEvent.LoadDrivers -> loadDrivers()
            is HomeEvent.RequestRide -> requestRide(event.request)
            is HomeEvent.NavigateBack -> router.navigateBack()
        }
    }

    private fun loadDrivers() {
        _state.update { UiState.Loading }
        viewModelScope.launch {
            interactor.getNearbyDrivers(LocationRequest(37.7749, -122.4194))
                .onSuccess { result ->
                    homeState = homeState.copy(drivers = result)
                }
                .onFailure { error ->
                    _state.update { UiState.Error(error.message) }
                }
        }
    }

    private fun requestRide(request: RideRequest) {
        _state.update { UiState.Loading }
        viewModelScope.launch {
            interactor.requestRide(request)
                .onSuccess { result ->
                    homeState = homeState.copy()
                    router.navigate(AppRoute.Ride.route)
                }
                .onFailure { error ->
                    _state.update { UiState.Error(error = error.message) }
                }
        }
    }
}