package com.shared.miniuber.feature.adaptive.component

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.saveable.mapSaver

internal val RideScrollStatesSaver = mapSaver(
    save = { states: MutableMap<String, ScrollState> ->
        states.mapValues { it.value.value }
    },
    restore = { saved ->
        saved.mapValues { ScrollState(initial = it.value as Int) }.toMutableMap()
    }
)
