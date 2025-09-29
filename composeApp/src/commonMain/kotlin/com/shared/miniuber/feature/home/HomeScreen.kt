// commonMain
package com.shared.miniuber.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.shared.miniuber.component.AimComponent
import com.shared.miniuber.component.LoadingComponent
import com.shared.miniuber.component.map.GoogleMapComponent
import com.shared.miniuber.core.base.UiState
import com.shared.miniuber.core.collectAsEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    presenter: HomeContract.Presenter = koinViewModel()
) {
    val uiState by presenter.state.collectAsState()
    var homeState by remember { mutableStateOf(HomeState()) }

    presenter.action.collectAsEffect {

    }

    when (val state = uiState) {
        is UiState.Init -> LaunchedEffect("init") {
            presenter.onEvent(HomeEvent.Init)
        }

        is UiState.Success -> {
            homeState = state.data
        }

        is UiState.Error -> Text("Error: ${state.message}")
        else -> {}
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMapComponent(
            modifier = Modifier.fillMaxSize(),
            state = homeState.mapState,
            onCameraChanged = { latLng ->
                presenter.onEvent(event = HomeEvent.OnMapStateUpdated(latLng))
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
            text = homeState.driversCountState.text
        )

        AimComponent(Modifier.align(Alignment.Center))


        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                onClick = { presenter.onEvent(HomeEvent.OnConfirmButtonClicked) },
                content = {
                    if (uiState is UiState.Loading)
                        LoadingComponent()
                    else
                        Text(text = stringResource(resource = homeState.confirmButtonState.text))
                },
            )

            homeState.cancelButtonState?.also { cancelState ->
                Button(
                    onClick = { presenter.onEvent(HomeEvent.OnCancelButtonClicked) },
                    content = {
                        Text(text = stringResource(resource = cancelState.text))
                    },
                )
            }
        }
    }
}
