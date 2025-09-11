// commonMain
package com.android.miniuber.domain.viper.router

import androidx.navigation.NavController
import com.android.miniuber.domain.viper.contract.BaseContract

class ComposeRouter(private val navController: NavController) : BaseContract.Router {
    override val navigator: NavController get() = navController
}
