package com.android.miniuber.feature.home

import com.android.miniuber.domain.model.DriverResponse

data class HomeState(
    val drivers: List<DriverResponse> = listOf()
)