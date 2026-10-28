package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shared.miniuber.feature.adaptive.model.DemoRide

@Composable
internal fun RideCardComponent(
    ride: DemoRide,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { this.selected = selected }
            .testTag(tag = ride.id),
        shape = RoundedCornerShape(size = 20.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) DemoColors.Accent else DemoColors.Border
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (selected) DemoColors.Selected else Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Text(
                text = "${ride.date} · ${ride.time}",
                style = MaterialTheme.typography.labelMedium,
                color = DemoColors.Muted
            )
            Text(
                text = ride.destination,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DemoColors.Ink
            )
            Text(
                text = "From ${ride.pickup}",
                style = MaterialTheme.typography.bodySmall,
                color = DemoColors.Muted
            )
            Text(
                text = ride.fare,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DemoColors.Accent
            )
            Text(
                text = "Completed",
                style = MaterialTheme.typography.labelSmall,
                color = DemoColors.Muted
            )
        }
    }
}
