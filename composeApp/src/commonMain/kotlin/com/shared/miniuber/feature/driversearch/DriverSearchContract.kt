package com.shared.miniuber.feature.driversearch

import com.shared.miniuber.core.base.BaseContract

interface DriverSearchContract : BaseContract {

    abstract class Presenter : BaseContract.Presenter<DriverSearchEvent, DriverSearchState, DriverSearchAction>()

    interface Interactor : BaseContract.Interactor {

    }

}