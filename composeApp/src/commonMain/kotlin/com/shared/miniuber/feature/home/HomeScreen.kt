package com.shared.miniuber.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.FilledTonalButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shared.miniuber.component.ActionButtonsComponent
import com.shared.miniuber.component.AimComponent
import com.shared.miniuber.component.ErrorMessageComponent
import com.shared.miniuber.component.IndicatorComponent
import com.shared.miniuber.component.map.GoogleMapComponent
import com.shared.miniuber.component.adaptive.PlatformBackHandler
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.drivers_count
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mapsConfigured: Boolean = true,
    presenter: HomeContract.Presenter = koinViewModel()
) {
    val uiState by presenter.state.collectAsState()
    val homeState = uiState.data
    LaunchedEffect(presenter) { presenter.onEvent(HomeEvent.Init) }
    PlatformBackHandler(enabled = true) { presenter.onEvent(HomeEvent.NavigateBack) }

    Box(modifier = modifier.fillMaxSize()) {
        if (mapsConfigured) GoogleMapComponent(
            modifier = Modifier.fillMaxSize(),
            state = homeState.mapState,
            onCameraChanged = { presenter.onEvent(HomeEvent.OnMapStateUpdated(it)) },
        )
        else Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainerLow) {
            Box(contentAlignment = Alignment.Center) { Text("Map unavailable", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        IndicatorComponent(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 48.dp),
            count = homeState.driversCountState.count?.let { stringResource(Res.string.drivers_count, it) } ?: "…",
        )
        AimComponent(
            modifier = Modifier.align(Alignment.Center).padding(bottom = 10.dp),
            visible = mapsConfigured && homeState.markerState.visible,
        )
        ActionButtonsComponent(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp).padding(horizontal = 16.dp).fillMaxWidth(),
            isLoading = uiState.isLoading,
            enabled = mapsConfigured,
            confirmText = stringResource(homeState.confirmButtonState.text),
            cancelText = homeState.cancelButtonState?.let { stringResource(it.text) },
            onConfirmClick = { presenter.onEvent(HomeEvent.OnConfirmButtonClicked) },
            onCancelClick = { presenter.onEvent(HomeEvent.OnCancelButtonClicked) },
        )
        uiState.error?.let { message ->
            ErrorMessageComponent(message = message)
        }
        FilledTonalButton(
            onClick = { presenter.onEvent(HomeEvent.NavigateBack) },
            modifier = Modifier.align(Alignment.TopStart).safeDrawingPadding().padding(12.dp),
        ) { Text("Home") }
    }
}
