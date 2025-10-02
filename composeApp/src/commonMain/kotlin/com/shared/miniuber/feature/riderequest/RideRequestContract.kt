package com.shared.miniuber.feature.riderequest

import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.domain.model.RideResponse

interface RideRequestContract : BaseContract {

    abstract class Presenter : BaseContract.Presenter<RideRequestEvent, RideRequestState, RideRequestAction>()

    interface Interactor : BaseContract.Interactor {
        suspend fun rideRequest(request: RideRequest): Result<RideResponse>
        suspend fun getLastRideRequest(): Result<RideResponse>
    }

}