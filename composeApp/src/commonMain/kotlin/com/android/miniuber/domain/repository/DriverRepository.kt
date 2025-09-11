package com.android.miniuber.domain.repository

import com.android.miniuber.domain.model.DriverResponse
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.model.RideResponse

interface DriverRepository {
    suspend fun fetchNearbyDrivers(location: LocationRequest): Result<List<DriverResponse>>
    suspend fun requestRide(request: RideRequest): Result<RideResponse>
}