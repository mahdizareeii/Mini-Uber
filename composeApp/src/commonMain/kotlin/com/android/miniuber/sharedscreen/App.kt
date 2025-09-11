package com.android.miniuber.sharedscreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.miniuber.domain.interactor.HomeInteractor
import com.android.miniuber.domain.viper.presenter.HomePresenter
import com.android.miniuber.domain.viper.router.ComposeRouter
import com.android.miniuber.repository.FakeDriverRepository
import com.android.miniuber.util.AppRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = AppRoute.Home.route
        ) {
            composable(route = AppRoute.Home.route) {
                HomeScreen(
                    presenter = HomePresenter(
                        interactor = HomeInteractor(repository = FakeDriverRepository()),
                        router = ComposeRouter(navController)
                    )
                )
            }

            composable(AppRoute.Ride.route) {
                RideScreen()
            }
        }
    }
}