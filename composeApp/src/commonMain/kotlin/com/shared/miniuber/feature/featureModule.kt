package com.shared.miniuber.feature

import com.shared.miniuber.core.ComposeRouter
import com.shared.miniuber.feature.adaptive.AdaptiveDemoContract
import com.shared.miniuber.feature.adaptive.AdaptiveDemoPresenter
import com.shared.miniuber.feature.riderequest.RideRequestContract
import com.shared.miniuber.feature.riderequest.RideRequestPresenter
import com.shared.miniuber.feature.home.HomeContract
import com.shared.miniuber.feature.home.HomePresenter
import com.shared.miniuber.feature.landing.LandingContract
import com.shared.miniuber.feature.landing.LandingPresenter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val featureModule = module {
    viewModel {
        LandingPresenter(
            router = get()
        )
    } bind LandingContract.Presenter::class

    viewModel {
        AdaptiveDemoPresenter(
            router = get()
        )
    } bind AdaptiveDemoContract.Presenter::class

    viewModel {
        HomePresenter(
            interactor = get(),
            router = get()
        )
    } bind HomeContract.Presenter::class

    viewModel {
        RideRequestPresenter(
            interactor = get(),
            router = get<ComposeRouter>().forScreen(savedStateHandle = get())
        )
    } bind RideRequestContract.Presenter::class
}
