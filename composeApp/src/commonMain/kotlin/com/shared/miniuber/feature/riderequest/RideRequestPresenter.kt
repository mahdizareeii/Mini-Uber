package com.shared.miniuber.feature.riderequest

import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.core.base.UiState
import kotlinx.coroutines.flow.update

class RideRequestPresenter(
    private val interactor: RideRequestContract.Interactor,
    private val router: BaseContract.Router
) : RideRequestContract.Presenter() {

    private var rideRequestState = RideRequestState()
        set(value) {
            field = value
            _state.update { UiState.Success(value) }
        }

    override fun onEvent(event: RideRequestEvent) {
        when (event) {
            is RideRequestEvent.Init -> {
                val argument = router.getScreenArg<AppScreens.RideRequestScreen>(
                    route = AppScreens.RideRequestScreen::class
                )
                rideRequestState = rideRequestState.copy(
                    screenArgument = argument,
                    nearDriverCount = argument?.pickupLat.toString(),
                )
                //check create a request with args
                //if was null so the serve returns last request if has value server registers new trip
            }
            is RideRequestEvent.NavigateBack -> router.navigateUp()
        }
    }

}