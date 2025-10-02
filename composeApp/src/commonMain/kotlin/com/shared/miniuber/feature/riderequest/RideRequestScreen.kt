package com.shared.miniuber.feature.riderequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.shared.miniuber.component.ErrorMessageComponent
import com.shared.miniuber.core.base.UiState
import com.shared.miniuber.core.collectAsEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RideRequestScreen(
    modifier: Modifier = Modifier,
    presenter: RideRequestContract.Presenter = koinViewModel()
) {
    val uiState by presenter.state.collectAsState()
    var rideRequestState by remember { mutableStateOf(RideRequestState()) }

    presenter.action.collectAsEffect {
        // Handle actions here
    }

    when (val state = uiState) {
        is UiState.Init -> LaunchedEffect("init") {
            presenter.onEvent(RideRequestEvent.Init)
        }

        is UiState.Success -> {
            rideRequestState = state.data
        }

        is UiState.Error -> ErrorMessageComponent(message = state.message)
        else -> {}
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(rideRequestState.nearDriverCount)
    }
}