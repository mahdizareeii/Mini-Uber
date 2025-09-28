@file:OptIn(KoinExperimentalAPI::class)

package com.shared.miniuber

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shared.miniuber.core.AppRoute
import com.shared.miniuber.core.ComposeRouter
import com.shared.miniuber.feature.home.HomeScreen
import com.shared.miniuber.feature.ride.RideScreen
import com.shared.miniuber.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
@Preview
fun App() {
    val isDarkMode = false
    val colors = if (isDarkMode)
        darkColorScheme(
            background = AppColors.Black,
            onBackground = AppColors.White,
            primary = AppColors.White,
            onPrimary = AppColors.Black
        )
    else
        lightColorScheme(
            background = AppColors.White,
            onBackground = AppColors.Black,
            primary = AppColors.Black,
            onPrimary = AppColors.White
        )
    MaterialTheme(
        colorScheme = colors
    ) {
        val navController = rememberNavController()
        //init App router
        getKoin().get<ComposeRouter>().initNavController(navController)

        NavHost(
            modifier = Modifier.windowInsetsPadding(
                insets = WindowInsets.ime.add(WindowInsets.navigationBars)
            ),
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