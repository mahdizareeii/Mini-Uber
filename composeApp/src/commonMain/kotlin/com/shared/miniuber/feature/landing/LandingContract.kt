package com.shared.miniuber.feature.landing

import com.shared.miniuber.core.base.BaseContract

interface LandingContract : BaseContract {
    abstract class Presenter : BaseContract.Presenter<LandingEvent, LandingState, LandingAction>(LandingState)
}
