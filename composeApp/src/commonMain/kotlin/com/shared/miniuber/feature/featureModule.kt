package com.shared.miniuber.feature

import com.shared.miniuber.feature.riderequest.RideRequestContract
import com.shared.miniuber.feature.riderequest.RideRequestPresenter
import com.shared.miniuber.feature.home.HomeContract
import com.shared.miniuber.feature.home.HomePresenter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        HomePresenter(
            interactor = get(),
            router = get()
        )
    } bind HomeContract.Presenter::class

    viewModel {
        RideRequestPresenter(
            interactor = get(),
            router = get()
        )
    } bind RideRequestContract.Presenter::class
}