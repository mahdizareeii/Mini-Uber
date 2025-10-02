package com.shared.miniuber.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

interface BaseContract {
    interface View<UI_STATE, UI_ACTION> {
        val _state: MutableStateFlow<UiState<UI_STATE>>
        val state: StateFlow<UiState<UI_STATE>>

        val _action: MutableSharedFlow<UI_ACTION>
        val action: SharedFlow<UI_ACTION>
    }

    abstract class Presenter<EVENT, UI_STATE, UI_ACTION>(
        initialState: UiState<UI_STATE> = UiState.Init
    ) : ViewModel(), View<UI_STATE, UI_ACTION> {
        override val _state = MutableStateFlow<UiState<UI_STATE>>(initialState)
        override val state: StateFlow<UiState<UI_STATE>> get() = _state

        override val _action: MutableSharedFlow<UI_ACTION> get() = MutableSharedFlow<UI_ACTION>()
        override val action: SharedFlow<UI_ACTION> get() = _action.asSharedFlow()

        abstract fun onEvent(event: EVENT)
    }

    interface Interactor

    interface Router {
        fun navigate(route: String)
        fun navigate(
            route: String,
            popUpTo: String,
            popUpToInclusive: Boolean
        )
        fun navigateBack()
    }
}