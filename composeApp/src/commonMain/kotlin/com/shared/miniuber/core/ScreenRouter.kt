package com.shared.miniuber.core

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.shared.miniuber.core.base.BaseContract
import kotlin.reflect.KClass

class ScreenRouter(
    router: BaseContract.Router,
    private val savedStateHandle: SavedStateHandle
) : BaseContract.Router by router {

    override fun <T : Any> getScreenArg(screen: KClass<T>): T {
        return savedStateHandle.toRoute(route = screen)
    }
}
