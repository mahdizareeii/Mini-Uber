package com.shared.miniuber.feature.home

import com.shared.miniuber.domain.model.DriverResponse

data class HomeState(
    val drivers: List<DriverResponse> = listOf()
)