package com.shared.miniuber.feature.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.shared.miniuber.feature.landing.component.DestinationCardComponent
import com.shared.miniuber.feature.landing.component.LandingHeaderComponent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    presenter: LandingContract.Presenter = koinViewModel()
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().safeDrawingPadding()
        ) {
            val isWide = maxWidth >= 600.dp
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = 1040.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                LandingHeaderComponent()
                if (isWide) {
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        DestinationCardComponent(
                            modifier = Modifier.weight(1f).testTag("open_map"),
                            number = "01",
                            title = "Map Demo",
                            description = "Pick your route and request a simulated ride.",
                            onClick = { presenter.onEvent(LandingEvent.OnMapDemoClicked) }
                        )
                        DestinationCardComponent(
                            modifier = Modifier.weight(1f).testTag("open_adaptive"),
                            number = "02",
                            title = "Adaptive Layout",
                            description = "Explore sample rides across phone, tablet, and folded layouts.",
                            onClick = { presenter.onEvent(LandingEvent.OnAdaptiveDemoClicked) }
                        )
                    }
                } else {
                    DestinationCardComponent(
                        modifier = Modifier.fillMaxWidth().testTag("open_map"),
                        number = "01",
                        title = "Map Demo",
                        description = "Pick your route and request a simulated ride.",
                        onClick = { presenter.onEvent(LandingEvent.OnMapDemoClicked) }
                    )
                    DestinationCardComponent(
                        modifier = Modifier.fillMaxWidth().testTag("open_adaptive"),
                        number = "02",
                        title = "Adaptive Layout",
                        description = "Explore sample rides across phone, tablet, and folded layouts.",
                        onClick = { presenter.onEvent(LandingEvent.OnAdaptiveDemoClicked) }
                    )
                }
                Text(
                    text = "Built with Kotlin & Compose Multiplatform",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
