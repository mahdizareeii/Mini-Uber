package com.android.miniuber.domain

import com.android.miniuber.domain.interactor.HomeInteractor
import com.android.miniuber.feature.home.HomeContract
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factory {
        HomeInteractor(
            repository = get()
        )
    } bind HomeContract.Interactor::class
}