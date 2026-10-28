package com.shared.miniuber.feature.adaptive

import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract
import kotlinx.coroutines.flow.update

class AdaptiveDemoPresenter(
    private val router: BaseContract.Router
) : AdaptiveDemoContract.Presenter() {

    override fun onEvent(event: AdaptiveDemoEvent) {
        when (event) {
            is AdaptiveDemoEvent.SelectRide -> selectRide(id = event.id)
            AdaptiveDemoEvent.BackToList -> clearSelection()
            AdaptiveDemoEvent.NavigateBack -> navigateHome()
        }
    }

    private fun selectRide(id: String) {
        _state.update { state ->
            if (state.data.rides.none { it.id == id }) {
                state
            } else {
                state.copy(data = state.data.copy(selectedRideId = id))
            }
        }
    }

    private fun clearSelection() {
        _state.update { state ->
            state.copy(data = state.data.copy(selectedRideId = null))
        }
    }

    private fun navigateHome() {
        router.navigate(
            route = AppScreens.LandingScreen,
            popUpTo = AppScreens.LandingScreen::class,
            popUpToInclusive = true
        )
    }
}
