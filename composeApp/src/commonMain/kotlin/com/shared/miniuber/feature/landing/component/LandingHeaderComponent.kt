package com.shared.miniuber.feature.landing.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LandingHeaderComponent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(text = "MINI UBER", style = MaterialTheme.typography.labelLarge)
        Text(
            text = "One app.\nMore possibilities.",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Explore the map experience or see how Compose adapts to your screen.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
