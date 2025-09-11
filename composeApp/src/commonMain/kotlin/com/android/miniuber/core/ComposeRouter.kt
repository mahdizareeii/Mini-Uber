package com.android.miniuber.core

import androidx.navigation.NavController
import com.android.miniuber.core.base.BaseContract

class ComposeRouter : BaseContract.Router {

    private var navController: NavController? = null

    fun initNavController(navController: NavController) {
        this.navController = navController
    }

    override fun navigate(route: String) {
        navController?.navigate(route)
    }

    override fun navigateBack() {
        navController?.navigateUp()
    }
}