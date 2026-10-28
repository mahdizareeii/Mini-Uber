package com.shared.miniuber.feature.landing

import com.shared.miniuber.core.AppScreens
import com.shared.miniuber.core.base.BaseContract

class LandingPresenter(
    private val router: BaseContract.Router
) : LandingContract.Presenter() {
    override fun onEvent(event: LandingEvent) {
        when (event) {
            LandingEvent.OnMapDemoClicked -> router.navigate(route = AppScreens.HomeScreen)
            LandingEvent.OnAdaptiveDemoClicked -> router.navigate(route = AppScreens.AdaptiveDemoScreen)
        }
    }
}
