package com.android.miniuber.core

import androidx.navigation.NavController
import com.android.miniuber.core.base.BaseContract

class ComposeRouter(private val navController: NavController) : BaseContract.Router {

    override fun navigate(route: String) = navController.navigate(route)
    override fun navigateBack() {
        navController.navigateUp()
    }
}