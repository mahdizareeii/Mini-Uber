package com.shared.miniuber.component.adaptive

import androidx.compose.runtime.Composable

@Composable
actual fun currentAdaptiveRegions(): List<AdaptiveRegion> = LocalAdaptiveWindowState.current.regions
