package com.android.miniuber.util

sealed class UiState<out T> {
    object Init : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(private val error: String?) : UiState<Nothing>() {
        val message get() = error ?: "Unknown Error"
    }
}