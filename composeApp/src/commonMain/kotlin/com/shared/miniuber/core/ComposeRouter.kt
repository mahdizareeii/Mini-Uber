package com.shared.miniuber.core

import androidx.navigation.NavController
import com.shared.miniuber.core.base.BaseContract

class ComposeRouter : BaseContract.Router {

    private var navController: NavController? = null

    fun initNavController(navController: NavController) {
        this.navController = navController
    }

    override fun navigate(route: String) {
        navController?.navigate(route)
    }

    override fun navigate(
        route: String,
        popUpTo: String,
        popUpToInclusive: Boolean
    ) {
        navController?.navigate(route) {
            popUpTo(popUpTo) {
                inclusive = popUpToInclusive
            }
        }
    }

    override fun navigateBack() {
        navController?.navigateUp()
    }
}