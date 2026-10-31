package com.shared.miniuber.feature.riderequest

import androidx.lifecycle.viewModelScope
import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
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
            is RideRequestEvent.NavigateHome -> router.navigate(
                route = AppScreens.LandingScreen,
                popUpTo = AppScreens.LandingScreen::class,
                popUpToInclusive = true
            )

            is RideRequestEvent.CancelRideRequest -> router.navigate(
                route = AppScreens.HomeScreen,
                popUpTo = AppScreens.RideRequestScreen::class,
                popUpToInclusive = true
            )
        }
    }

    private fun registerRideRequest() {
        _state.update { it.copy(isLoading = true, error = null) }
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
            ).onSuccess { rideResponse ->
                _state.update {
                    it.copy(
                        data = it.data.copy(requestedAt = rideResponse.requestedAt),
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun getLastRideRequest() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            interactor.getLastRideRequest().onSuccess { rideResponse ->
                _state.update {
                    it.copy(
                        data = it.data.copy(requestedAt = rideResponse.requestedAt),
                        isLoading = false,
                        error = null
                    )
                }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

}
