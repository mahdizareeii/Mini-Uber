package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shared.miniuber.component.adaptive.AdaptiveLayoutMode
import com.shared.miniuber.feature.adaptive.model.DemoRide

@Composable
internal fun RideListComponent(
    rides: List<DemoRide>,
    selectedId: String?,
    scrollState: LazyListState,
    mode: AdaptiveLayoutMode,
    onSelect: (String) -> Unit,
    onNavigateHome: () -> Unit
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        item(key = "heading") {
            RideListHeaderComponent(
                mode = mode,
                onNavigateHome = onNavigateHome
            )
        }
        items(items = rides, key = { it.id }) { ride ->
            RideCardComponent(
                ride = ride,
                selected = ride.id == selectedId,
                onClick = { onSelect(ride.id) }
            )
        }
        item(key = "footer") {
            Text(
                text = "Resize, rotate or fold your device. Your selection and scroll position stay with you.",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = DemoColors.Muted
            )
        }
    }
}
