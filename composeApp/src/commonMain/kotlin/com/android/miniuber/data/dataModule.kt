package com.android.miniuber.data

import com.android.miniuber.data.repository.FakeDriverRepositoryImpl
import com.android.miniuber.domain.repository.DriverRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factory {
        FakeDriverRepositoryImpl()
    } bind DriverRepository::class
}