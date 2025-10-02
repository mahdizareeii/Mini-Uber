// commonMain
package com.shared.miniuber.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import com.shared.miniuber.component.ActionButtonsComponent
import com.shared.miniuber.component.AimComponent
import com.shared.miniuber.component.IndicatorComponent
import com.shared.miniuber.component.ErrorMessageComponent
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
        // Handle actions here
    }

    when (val state = uiState) {
        is UiState.Init -> LaunchedEffect("init") {
            presenter.onEvent(HomeEvent.Init)
        }

        is UiState.Success -> {
            homeState = state.data
        }

        is UiState.Error -> ErrorMessageComponent(message = state.message)
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

        IndicatorComponent(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp),
            count = homeState.driversCountState.text
        )

        AimComponent(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 30.dp)
        )

        ActionButtonsComponent(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            isLoading = uiState is UiState.Loading,
            confirmText = stringResource(resource = homeState.confirmButtonState.text),
            cancelText = homeState.cancelButtonState?.let { stringResource(resource = it.text) },
            onConfirmClick = { presenter.onEvent(HomeEvent.OnConfirmButtonClicked) },
            onCancelClick = { presenter.onEvent(HomeEvent.OnCancelButtonClicked) }
        )
    }
}
