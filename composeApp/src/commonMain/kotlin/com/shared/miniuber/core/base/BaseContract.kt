package com.shared.miniuber.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.reflect.KClass

interface BaseContract {
    interface View<UI_STATE, UI_ACTION> {
        val state: StateFlow<UiState<UI_STATE>>
        val action: SharedFlow<UI_ACTION>
    }

    abstract class Presenter<EVENT, UI_STATE, UI_ACTION>(
        initialState: UI_STATE
    ) : ViewModel(), View<UI_STATE, UI_ACTION> {
        protected val _state = MutableStateFlow(UiState(initialState))
        override val state = _state.asStateFlow()

        protected val _action = MutableSharedFlow<UI_ACTION>()
        override val action = _action.asSharedFlow()

        abstract fun onEvent(event: EVENT)
    }

    interface Interactor

    interface Router {
        fun <RESULT> setParentResult(key: String, value: RESULT)
        fun <RESULT> getResultFlow(key: String, initialValue: RESULT?): StateFlow<RESULT?>?

        fun <T : Any> getScreenArg(screen: KClass<T>): T

        fun navigate(route: String)
        fun navigate(route: String, popUpTo: String, popUpToInclusive: Boolean)

        fun <T : Any> navigate(route: T)
        fun <T : Any> navigate(route: T, popUpTo: KClass<*>, popUpToInclusive: Boolean)

        fun navigateUp()
    }
}
