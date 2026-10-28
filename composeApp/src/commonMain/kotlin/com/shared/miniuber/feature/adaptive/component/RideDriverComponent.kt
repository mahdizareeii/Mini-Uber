package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun RideDriverComponent(
    driver: String,
    vehicle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Text(
            text = "Your driver",
            style = MaterialTheme.typography.labelLarge,
            color = DemoColors.Muted
        )
        Text(
            text = driver,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = DemoColors.Ink
        )
        Text(
            text = vehicle,
            style = MaterialTheme.typography.bodyMedium,
            color = DemoColors.Muted
        )
    }
}
