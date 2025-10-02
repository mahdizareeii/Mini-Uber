package com.shared.miniuber.feature.riderequest

import androidx.lifecycle.viewModelScope
import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.core.base.UiState
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.RideRequest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RideRequestPresenter(
    private val interactor: RideRequestContract.Interactor,
    private val router: BaseContract.Router
) : RideRequestContract.Presenter() {

    private val argument = router.getScreenArg<AppScreens.RideRequestScreen>(
        screen = AppScreens.RideRequestScreen::class
    )

    private var rideRequestState = RideRequestState()
        set(value) {
            field = value
            _state.update { UiState.Success(value) }
        }

    override fun onEvent(event: RideRequestEvent) {
        when (event) {
            is RideRequestEvent.Init -> {
                if (argument.isValidLocation()) {
                    registerRideRequest()
                } else {
                    getLastRideRequest()
                }
            }
            is RideRequestEvent.NavigateBack -> router.navigateUp()
            is RideRequestEvent.CancelRideRequest -> router.navigate(
                route = AppScreens.HomeScreen,
                popUpTo = AppScreens.RideRequestScreen::class,
                popUpToInclusive = true
            )
        }
    }

    private fun registerRideRequest() {
        _state.update { UiState.Loading }
        viewModelScope.launch {
            interactor.rideRequest(
                request = RideRequest(
                    pickup = LocationRequest(
                        lat = argument.pickupLat,
                        lng = argument.pickupLng
                    ),
                    dropOff = LocationRequest(
                        lat = argument.dropOffLat,
                        lng = argument.dropOffLng
                    ),
                )
            ).onSuccess {
                rideRequestState = rideRequestState.copy(requestedAt = it.requestedAt)
            }.onFailure { error ->
                _state.update { UiState.Error(error = error.message) }
            }
        }
    }

    private fun getLastRideRequest() {
        _state.update { UiState.Loading }
        viewModelScope.launch {
            interactor.getLastRideRequest().onSuccess {
                rideRequestState = rideRequestState.copy(requestedAt = it.requestedAt)
            }.onFailure { error ->
                _state.update { UiState.Error(error = error.message) }
            }
        }
    }

}