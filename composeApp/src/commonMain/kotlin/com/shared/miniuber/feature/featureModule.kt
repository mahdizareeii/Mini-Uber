package com.shared.miniuber.feature

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
}