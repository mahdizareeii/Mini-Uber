package com.shared.miniuber.component.adaptive

import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalAdaptiveWindowState = staticCompositionLocalOf<AdaptiveWindowState> {
    error("An adaptive window state must be supplied by MainViewController")
}
