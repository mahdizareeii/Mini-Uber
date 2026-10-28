package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.background
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shared.miniuber.feature.adaptive.model.DemoRide

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun RideDetailComponent(
    ride: DemoRide,
    scrollState: ScrollState,
    isCompact: Boolean,
    onBackToList: () -> Unit,
    onNavigateHome: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(all = 24.dp),
            verticalArrangement = Arrangement.spacedBy(space = 24.dp)
        ) {
            if (isCompact) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    OutlinedButton(onClick = onBackToList) {
                        Text(text = "All rides")
                    }
                    TextButton(onClick = onNavigateHome) {
                        Text(text = "Home")
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(space = 12.dp)) {
                DemoLabelComponent(text = "COMPLETED RIDE")
                Text(
                    text = ride.destination,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = DemoColors.Ink
                )
                Text(
                    text = "${ride.date} · ${ride.time}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DemoColors.Muted
                )
            }
            RideFareComponent(fare = ride.fare)
            Column {
                RouteStopComponent(label = "PICKUP", location = ride.pickup, outlined = true)
                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .height(height = 28.dp)
                        .width(width = 2.dp)
                        .background(color = DemoColors.Border)
                )
                RouteStopComponent(label = "DESTINATION", location = ride.destination, outlined = false)
            }
            HorizontalDivider(color = DemoColors.Border)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                DetailFactComponent(
                    label = "Distance",
                    value = ride.distance,
                    modifier = Modifier.weight(weight = 1f)
                )
                DetailFactComponent(
                    label = "Journey time",
                    value = ride.duration,
                    modifier = Modifier.weight(weight = 1f)
                )
            }
            HorizontalDivider(color = DemoColors.Border)
            RideDriverComponent(driver = ride.driver, vehicle = ride.vehicle)
            Surface(
                shape = RoundedCornerShape(size = 16.dp),
                color = DemoColors.Background
            ) {
                Text(
                    text = "Adaptive layout demo\nThese rides are local examples. No booking or payment is made here.",
                    modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = DemoColors.Muted
                )
            }
        }
    }
}
