package com.shared.miniuber.data.repository

import com.shared.miniuber.domain.model.DriverResponse
import com.shared.miniuber.domain.model.LocationRequest
import com.shared.miniuber.domain.model.LocationResponse
import com.shared.miniuber.domain.model.RideRequest
import com.shared.miniuber.domain.model.RideResponse
import com.shared.miniuber.domain.model.TripResponse
import com.shared.miniuber.domain.repository.DriverRepository
import com.shared.miniuber.getPlatform
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeDriverRepositoryImpl(
    private val dispatcher: CoroutineDispatcher
) : DriverRepository {

    override suspend fun fetchNearbyDrivers(location: LocationRequest): Result<List<DriverResponse>> {
        delay(1000)
        return withContext(dispatcher) {
            Result.success(
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
    }

    override suspend fun requestRide(request: RideRequest): Result<RideResponse> {
        delay(1000)
        return withContext(dispatcher) {
            Result.success(
                value = RideResponse(
                    id = "ride-${getPlatform().generateUuid()}",
                    pickup = request.pickup,
                    dropOff = request.dropOff,
                )
            )
        }
    }

    override suspend fun getLastTrip(): Result<TripResponse> {
        delay(1000L)
        return withContext(dispatcher) {
            Result.success(
                value = TripResponse(
                    state = TripResponse.TripState.NoTrip
                )
            )
        }
    }
}