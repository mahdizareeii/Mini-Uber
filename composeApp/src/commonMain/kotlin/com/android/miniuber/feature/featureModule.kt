package com.android.miniuber.feature

import com.android.miniuber.feature.home.HomeContract
import com.android.miniuber.feature.home.HomePresenter
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