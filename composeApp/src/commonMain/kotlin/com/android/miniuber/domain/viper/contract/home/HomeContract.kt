// commonMain
package com.android.miniuber.domain.viper.contract.home

import com.android.miniuber.domain.model.DriverResponse
import com.android.miniuber.domain.model.LocationRequest
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.model.RideResponse
import com.android.miniuber.domain.viper.contract.BaseContract
import com.android.miniuber.util.UiState
import kotlinx.coroutines.flow.update

sealed interface HomeEvent {
    data object LoadDrivers : HomeEvent
    data class RequestRide(val request: RideRequest) : HomeEvent
    data object NavigateBack : HomeEvent
}

data class HomeUiState(
    val drivers: List<DriverResponse> = listOf()
)

interface HomeContract {
    abstract class Presenter : BaseContract.Presenter<HomeEvent, HomeUiState>() {
        protected var homeUiState = HomeUiState()
            set(value) {
                field = value
                _uiState.update { UiState.Success(value) }
            }
    }

    abstract class Interactor : BaseContract.Interactor {
        abstract suspend fun getNearbyDrivers(location: LocationRequest): Result<List<DriverResponse>>
        abstract suspend fun requestRide(request: RideRequest): Result<RideResponse>
    }
}
