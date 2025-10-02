package com.shared.miniuber.domain.repository

import com.shared.miniuber.domain.model.DriverResponse
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.domain.model.RideResponse
import com.shared.miniuber.domain.model.TripResponse

interface DriverRepository {
    suspend fun getLastTrip(): Result<TripResponse>
    suspend fun fetchNearbyDrivers(location: LocationRequest): Result<List<DriverResponse>>
    suspend fun requestRide(request: RideRequest): Result<RideResponse>
}