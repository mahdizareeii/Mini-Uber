package com.shared.miniuber.core.base

/** Retains screen content while loading or displaying a recoverable error. */
data class UiState<T>(
    val data: T,
    val isLoading: Boolean = false,
    val error: String? = null,
)
