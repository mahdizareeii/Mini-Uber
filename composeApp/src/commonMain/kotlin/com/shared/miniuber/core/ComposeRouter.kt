package com.shared.miniuber.core

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.shared.miniuber.core.base.BaseContract
import kotlin.reflect.KClass

class ComposeRouter : BaseContract.Router {

    private var navController: NavController? = null
    var backStackEntry: NavBackStackEntry? = null

    fun initNavController(navController: NavController) {
        this.navController = navController
    }

    fun setCurrentBackStackEntry(backStackEntry: NavBackStackEntry) {
        this.backStackEntry = backStackEntry
    }

    override fun <T> getScreenArg(route: KClass<*>): T? {
        return backStackEntry?.toRoute(route)
    }

    override fun navigate(route: String) {
        navController?.navigate(route)
    }

    override fun navigate(
        route: String,
        popUpTo: String,
        popUpToInclusive: Boolean
    ) {
        navController?.navigate(route = route) {
            popUpTo(popUpTo) {
                inclusive = popUpToInclusive
            }
        }
    }

    override fun <T : Any> navigate(route: T) {
        navController?.navigate(route)
    }

    override fun <T : Any> navigate(
        route: T,
        popUpTo: KClass<*>,
        popUpToInclusive: Boolean
    ) {
        navController?.navigate(route = route) {
            popUpTo(popUpTo) {
                inclusive = popUpToInclusive
            }
        }
    }


    override fun navigateUp() {
        navController?.navigateUp()
    }
}