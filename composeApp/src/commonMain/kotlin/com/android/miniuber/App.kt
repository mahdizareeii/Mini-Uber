package com.android.miniuber

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.miniuber.domain.interactor.HomeInteractor
import com.android.miniuber.feature.home.HomePresenter
import com.android.miniuber.core.ComposeRouter
import com.android.miniuber.data.repository.FakeDriverRepositoryImpl
import com.android.miniuber.core.AppRoute
import com.android.miniuber.feature.home.HomeScreen
import com.android.miniuber.feature.ride.RideScreen
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
                        interactor = HomeInteractor(repository = FakeDriverRepositoryImpl()),
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