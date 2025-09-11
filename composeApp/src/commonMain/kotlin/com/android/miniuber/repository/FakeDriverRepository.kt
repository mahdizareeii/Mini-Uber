package com.android.miniuber.repository

import com.android.miniuber.domain.model.DriverResponse
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.LocationResponse
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.model.RideResponse
import com.android.miniuber.domain.repository.DriverRepository
import com.android.miniuber.util.getPlatform
import kotlinx.coroutines.delay

class FakeDriverRepository : DriverRepository {

    override suspend fun fetchNearbyDrivers(location: LocationRequest): Result<List<DriverResponse>> {
        delay(1000)
        return Result.success(
            value = listOf(
                DriverResponse(
                    id = "d1",
                    name = "Alice",
                    vehicle = "Toyota Prius",
                    location = LocationResponse(location.lat + 0.001, location.lng + 0.002),
                    etaMinutes = 3
                ),
                DriverResponse(
                    id = "d2",
                    name = "Bob",
                    vehicle = "Honda Civic",
                    location = LocationResponse(location.lat - 0.001, location.lng - 0.004),
                    etaMinutes = 5
                )
            )
        )
    }

    override suspend fun requestRide(request: RideRequest): Result<RideResponse> {
        delay(1000)
        return Result.success(
            value = RideResponse(
                id = "ride-${getPlatform().generateUuid()}",
                pickup = request.pickup,
                dropOff = request.dropOff,
            )
        )
    }
}