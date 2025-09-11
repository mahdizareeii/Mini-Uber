// commonMain
package com.android.miniuber.feature.home

import com.android.miniuber.core.base.BaseContract
import com.android.miniuber.core.base.UiState
import com.android.miniuber.domain.model.DriverResponse
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.model.RideResponse
import kotlinx.coroutines.flow.update

interface HomeContract : BaseContract {
    abstract class Presenter : BaseContract.Presenter<HomeEvent, HomeState>()
    interface Interactor : BaseContract.Interactor {
        suspend fun getNearbyDrivers(
            location: LocationRequest
        ): Result<List<DriverResponse>>

        suspend fun requestRide(
            request: RideRequest
        ): Result<RideResponse>
    }
}
