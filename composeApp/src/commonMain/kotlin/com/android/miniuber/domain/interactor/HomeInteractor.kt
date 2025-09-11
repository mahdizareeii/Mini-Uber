package com.android.miniuber.domain.interactor

import com.android.miniuber.domain.model.DriverResponse
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.model.RideResponse
import com.android.miniuber.domain.repository.DriverRepository
import com.android.miniuber.feature.home.HomeContract

//can separate concern of business logics by injecting use case
//Handle business login here like (use cases)
class HomeInteractor(private val repository: DriverRepository) : HomeContract.Interactor() {
    override suspend fun getNearbyDrivers(
        location: LocationRequest
    ): Result<List<DriverResponse>> {
        return repository.fetchNearbyDrivers(location)
    }

    override suspend fun requestRide(
        request: RideRequest
    ): Result<RideResponse> {
        return repository.requestRide(request)
    }
}