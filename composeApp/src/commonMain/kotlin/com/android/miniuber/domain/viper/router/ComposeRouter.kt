// commonMain
package com.android.miniuber.domain.viper.router

import androidx.navigation.NavController
import com.android.miniuber.domain.viper.contract.BaseContract

class ComposeRouter(navController: NavController) : BaseContract.Router {
    override val navController: NavController get() = navController
}
