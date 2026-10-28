package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shared.miniuber.component.adaptive.AdaptiveLayoutMode

@Composable
internal fun RideListHeaderComponent(
    mode: AdaptiveLayoutMode,
    onNavigateHome: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 12.dp)) {
        OutlinedButton(onClick = onNavigateHome) {
            Text(text = "Home")
        }
        DemoLabelComponent(
            text = when (mode) {
                AdaptiveLayoutMode.SIDE_BY_SIDE -> "SIDE BY SIDE"
                AdaptiveLayoutMode.TABLETOP -> "TABLETOP"
                else -> "COMPACT"
            }
        )
        Text(
            text = "Your rides",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = DemoColors.Ink
        )
        Text(
            text = "Six little journeys. One flexible layout.",
            style = MaterialTheme.typography.bodyMedium,
            color = DemoColors.Muted
        )
        Text(
            text = "LOCAL SAMPLE DATA",
            style = MaterialTheme.typography.labelSmall,
            color = DemoColors.Muted
        )
        Spacer(modifier = Modifier.height(height = 4.dp))
    }
}
