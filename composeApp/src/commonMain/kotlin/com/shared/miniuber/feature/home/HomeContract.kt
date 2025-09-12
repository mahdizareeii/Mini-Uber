// commonMain
package com.shared.miniuber.feature.home

import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.domain.model.DriverResponse
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.domain.model.RideResponse

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
