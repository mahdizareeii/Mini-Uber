package com.shared.miniuber.domain.interactor

import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.domain.model.RideResponse
import com.shared.miniuber.domain.repository.DriverRepository
import com.shared.miniuber.feature.riderequest.RideRequestContract

class DriverSearchInteractor(
    private val repository: DriverRepository
): RideRequestContract.Interactor {
    override suspend fun rideRequest(request: RideRequest): Result<RideResponse> {
        return repository.requestRide(request)
    }
}