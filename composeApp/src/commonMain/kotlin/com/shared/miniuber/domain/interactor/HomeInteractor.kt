package com.shared.miniuber.domain.interactor

import com.shared.miniuber.domain.model.DriverResponse
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.TripResponse
import com.shared.miniuber.domain.repository.DriverRepository
import com.shared.miniuber.feature.home.HomeContract

//can separate concern of business logics by injecting use case
//Handle business login here like (use cases)
class HomeInteractor(private val repository: DriverRepository) : HomeContract.Interactor {
    override suspend fun getNearbyDrivers(
        location: LocationRequest
    ): Result<List<DriverResponse>> {
        return repository.fetchNearbyDrivers(location)
    }

    override suspend fun getLastTrip(): Result<TripResponse> {
        return repository.getLastTrip()
    }
}