package com.android.miniuber.feature.home

import androidx.lifecycle.viewModelScope
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.core.base.BaseContract
import com.android.miniuber.core.AppRoute
import com.android.miniuber.core.base.UiState
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomePresenter(
    private val interactor: HomeContract.Interactor,
    private val router: BaseContract.Router
) : HomeContract.Presenter() {

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadDrivers -> loadDrivers()
            is HomeEvent.RequestRide -> requestRide(event.request)
            is HomeEvent.NavigateBack -> router.navigateBack()
        }
    }

    private fun loadDrivers() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            interactor.getNearbyDrivers(LocationRequest(37.7749, -122.4194))
                .onSuccess { result ->
                    homeState = homeState.copy(drivers = result)
                }
                .onFailure { error ->
                    _uiState.update { UiState.Error(error.message) }
                }
        }
    }

    private fun requestRide(request: RideRequest) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            interactor.requestRide(request)
                .onSuccess { result ->
                    router.navigate(AppRoute.Ride.route)
                }
                .onFailure { error ->
                    _uiState.update { UiState.Error(error = error.message) }
                }
        }
    }
}