package com.shared.miniuber.core

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.shared.miniuber.core.base.BaseContract
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

class ComposeRouter : BaseContract.Router {

    private var navController: NavController? = null
    var lastBackStackEntry: NavBackStackEntry? =null

    fun initNavController(navController: NavController) {
        this.navController = navController
    }

    fun setCurrentBackStackEntry(backStackEntry: NavBackStackEntry) {
        this.lastBackStackEntry = backStackEntry
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

    override fun <T> getScreenArg(screen: KClass<*>): T {
        return lastBackStackEntry?.toRoute(route = screen) ?: error("No back stack entry available for screen $screen call setCurrentBackStackEntry in composable fun of your NavHost")
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