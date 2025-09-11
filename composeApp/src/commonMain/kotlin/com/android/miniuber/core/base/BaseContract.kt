package com.android.miniuber.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract {
    interface View<UI_STATE> {
        val _state: MutableStateFlow<UiState<UI_STATE>>
        val state: StateFlow<UiState<UI_STATE>>
    }

    abstract class Presenter<EVENT, UI_STATE>(
        initialState: UiState<UI_STATE> = UiState.Init
    ) : ViewModel(), View<UI_STATE> {
        override val _state = MutableStateFlow<UiState<UI_STATE>>(initialState)
        override val state: StateFlow<UiState<UI_STATE>> get() = _state
        abstract fun onEvent(event: EVENT)
    }

    interface Interactor

    interface Router {
        fun navigate(route: String)
        fun navigateBack()
    }
}