package com.shared.miniuber.component.adaptive

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // iOS has no system back event. Shared screens provide explicit back controls.
}
