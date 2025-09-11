@file:OptIn(KoinExperimentalAPI::class)

package com.android.miniuber

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.miniuber.core.AppRoute
import com.android.miniuber.core.ComposeRouter
import com.android.miniuber.data.repository.FakeDriverRepositoryImpl
import com.android.miniuber.domain.interactor.HomeInteractor
import com.android.miniuber.feature.home.HomePresenter
import com.android.miniuber.feature.home.HomeScreen
import com.android.miniuber.feature.ride.RideScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.getKoin
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.scope.rememberKoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinApplication

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        //init App router
        getKoin().get<ComposeRouter>().initNavController(navController)

        NavHost(
            navController = navController,
            startDestination = AppRoute.Home.route
        ) {
            composable(route = AppRoute.Home.route) {
                HomeScreen()
            }

            composable(AppRoute.Ride.route) {
                RideScreen()
            }
        }
    }
}