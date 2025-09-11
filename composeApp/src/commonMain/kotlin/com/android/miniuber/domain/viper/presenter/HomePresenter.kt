package com.android.miniuber.domain.viper.presenter

import androidx.lifecycle.viewModelScope
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.viper.contract.BaseContract
import com.android.miniuber.domain.viper.contract.home.HomeContract
import com.android.miniuber.domain.viper.contract.home.HomeEvent
import com.android.miniuber.util.AppRoute
import com.android.miniuber.util.UiState
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
            is HomeEvent.NavigateBack -> router.navigator.navigateUp()
        }
    }

    private fun loadDrivers() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            val result = interactor.getNearbyDrivers(LocationRequest(37.7749, -122.4194))
            if (result.isSuccess) {
                homeUiState = homeUiState.copy(
                    drivers = result.getOrNull() ?: listOf()
                )
            } else {
                _uiState.update { UiState.Error(result.exceptionOrNull()?.message) }
            }
        }
    }

    private fun requestRide(request: RideRequest) {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            val result = interactor.requestRide(request)
            if (result.isSuccess) {
                router.navigator.navigate(AppRoute.Ride.route)
            } else {
                _uiState.update { UiState.Error(error = result.exceptionOrNull()?.message) }
            }
        }
    }
}