package com.shared.miniuber.core

import androidx.lifecycle.SavedStateHandle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ScreenRouterTest {

    @Test
    fun screenArgumentsRemainBoundToTheirDestination() {
        val navigation = ComposeRouter()
        val firstScreen = navigation.forScreen(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "pickupLat" to 0.0,
                    "pickupLng" to 0.0,
                    "dropOffLat" to 35.1,
                    "dropOffLng" to 51.1
                )
            )
        )
        val secondScreen = navigation.forScreen(
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "pickupLat" to 40.0,
                    "pickupLng" to 50.0,
                    "dropOffLat" to 40.1,
                    "dropOffLng" to 50.1
                )
            )
        )

        val secondArgument = secondScreen.getScreenArg(screen = AppScreens.RideRequestScreen::class)
        val firstArgument = firstScreen.getScreenArg(screen = AppScreens.RideRequestScreen::class)

        assertEquals(AppScreens.RideRequestScreen(40.0, 50.0, 40.1, 50.1), secondArgument)
        assertEquals(AppScreens.RideRequestScreen(0.0, 0.0, 35.1, 51.1), firstArgument)
    }

    @Test
    fun routeDecodingRequiresTheOriginalCoordinateFields() {
        val router = ComposeRouter().forScreen(savedStateHandle = SavedStateHandle())

        assertFails {
            router.getScreenArg(screen = AppScreens.RideRequestScreen::class)
        }
    }
}
