package com.shared.miniuber.core

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.shared.miniuber.core.base.BaseContract
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

class ComposeRouter : BaseContract.Router {

    private var navController: NavController? = null

    fun initNavController(navController: NavController) {
        this.navController = navController
    }

    fun clearNavController(controller: NavController) {
        if (navController === controller) navController = null
    }

    fun forScreen(savedStateHandle: SavedStateHandle): BaseContract.Router {
        return ScreenRouter(
            router = this,
            savedStateHandle = savedStateHandle
        )
    }

    override fun <T : Any> getScreenArg(screen: KClass<T>): T {
        error("Use a destination-scoped router to read arguments for $screen")
    }

    override fun <RESULT> setParentResult(key: String, value: RESULT) {
        navController?.previousBackStackEntry?.savedStateHandle?.set(key = key, value = value)
    }

    override fun <RESULT> getResultFlow(key: String, initialValue: RESULT?): StateFlow<RESULT?>? {
        return navController?.currentBackStackEntry?.savedStateHandle?.getStateFlow(
            key = key,
            initialValue = initialValue
        )
    }

    override fun navigate(route: String) {
        navController?.navigate(route = route)
    }

    override fun navigate(
        route: String,
        popUpTo: String,
        popUpToInclusive: Boolean
    ) {
        navController?.navigate(route = route) {
            popUpTo(route = popUpTo) {
                inclusive = popUpToInclusive
            }
        }
    }

    override fun <T : Any> navigate(route: T) {
        navController?.navigate(route = route)
    }

    override fun <T : Any> navigate(
        route: T,
        popUpTo: KClass<*>,
        popUpToInclusive: Boolean
    ) {
        navController?.navigate(route = route) {
            popUpTo(route = popUpTo) {
                inclusive = popUpToInclusive
            }
        }
    }


    override fun navigateUp() {
        navController?.navigateUp()
    }
}
