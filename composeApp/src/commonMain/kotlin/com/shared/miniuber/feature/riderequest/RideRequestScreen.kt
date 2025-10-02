package com.shared.miniuber.feature.riderequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shared.miniuber.component.ErrorMessageComponent
import com.shared.miniuber.component.WaitingAnimationComponent
import com.shared.miniuber.core.base.UiState
import com.shared.miniuber.core.collectAsEffect
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.cancel_ride
import miniuber.composeapp.generated.resources.finding_your_driver
import miniuber.composeapp.generated.resources.your_ride_requested
import org.jetbrains.compose.resources.stringResource
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

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WaitingAnimationComponent()

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.finding_your_driver),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.your_ride_requested),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { presenter.onEvent(RideRequestEvent.CancelRideRequest) },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(Res.string.cancel_ride))
                    }
                }
            }
        }
    }
}