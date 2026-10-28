package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun RouteStopComponent(
    label: String,
    location: String,
    outlined: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Surface(
            modifier = Modifier.size(size = 12.dp),
            shape = CircleShape,
            color = if (outlined) Color.White else DemoColors.Accent,
            border = BorderStroke(width = 2.dp, color = DemoColors.Accent)
        ) {}
        Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = DemoColors.Muted
            )
            Text(
                text = location,
                style = MaterialTheme.typography.titleMedium,
                color = DemoColors.Ink
            )
        }
    }
}
