package com.shared.miniuber.domain

import com.shared.miniuber.domain.interactor.DriverSearchInteractor
import com.shared.miniuber.domain.interactor.HomeInteractor
import com.shared.miniuber.feature.riderequest.RideRequestContract
import com.shared.miniuber.feature.home.HomeContract
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory {
        HomeInteractor(
            repository = get()
        )
    } bind HomeContract.Interactor::class

    factory {
        DriverSearchInteractor(
            repository = get()
        )
    } bind RideRequestContract.Interactor::class
}