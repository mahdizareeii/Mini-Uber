// commonMain
package com.android.miniuber.sharedscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.miniuber.domain.model.RideRequest
import com.android.miniuber.domain.viper.contract.home.HomeContract
import com.android.miniuber.domain.viper.contract.home.HomeEvent
import com.android.miniuber.domain.viper.contract.home.HomeUiState
import com.android.miniuber.util.UiState

@Composable
fun HomeScreen(presenter: HomeContract.Presenter) {
    val uiState by presenter.uiState.collectAsState()

    var homeUiState by remember { mutableStateOf(HomeUiState()) }

    when (val state = uiState) {
        is UiState.Init -> {
            LaunchedEffect("init") { presenter.onEvent(HomeEvent.LoadDrivers) }
        }

        is UiState.Success -> {
            homeUiState = state.data
        }

        is UiState.Error -> Text("Error: ${state.message}")
        else -> {}
    }

    if (uiState is UiState.Loading) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        homeUiState.drivers.forEach { d ->
            Text("${d.name} - ETA ${d.etaMinutes}min")
            Button(
                onClick = {
                    presenter.onEvent(
                        event = HomeEvent.RequestRide(
                            request = RideRequest(
                                pickup = d.location,
                                dropOff = d.location
                            )
                        )
                    )
                }
            ) {
                Text("Request Ride")
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
