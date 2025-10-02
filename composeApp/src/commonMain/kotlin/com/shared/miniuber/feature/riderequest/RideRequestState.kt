package com.shared.miniuber.feature.riderequest

import com.shared.miniuber.core.AppScreens

data class RideRequestState(
    val screenArgument: AppScreens.RideRequestScreen? = null,
    val nearDriverCount: String = ""
)
