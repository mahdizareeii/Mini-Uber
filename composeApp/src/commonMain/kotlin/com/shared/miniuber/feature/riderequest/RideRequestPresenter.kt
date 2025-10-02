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
                rideRequestState = rideRequestState.copy(
                    nearDriverCount = argument.pickupLat.toString(),
                )
                when (argument.requestMode) {
                    AppScreens.RideRequestScreen.RequestMode.GetPreviousRequest -> TODO()
                    AppScreens.RideRequestScreen.RequestMode.RegisterNewRequest -> registerRideRequest()
                }


                //check create a request with args
                //if was null so the serve returns last request if has value server registers new trip
            }

            is RideRequestEvent.NavigateBack -> router.navigateUp()
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
                rideRequestState = rideRequestState.copy(

                )
            }.onFailure {

            }
        }
    }

}