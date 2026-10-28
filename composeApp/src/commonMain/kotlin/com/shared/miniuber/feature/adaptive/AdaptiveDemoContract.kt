package com.shared.miniuber.feature.adaptive

import com.shared.miniuber.core.base.BaseContract

interface AdaptiveDemoContract : BaseContract {
    abstract class Presenter : BaseContract.Presenter<AdaptiveDemoEvent, AdaptiveDemoState, AdaptiveDemoAction>(
        initialState = AdaptiveDemoState()
    )
}
