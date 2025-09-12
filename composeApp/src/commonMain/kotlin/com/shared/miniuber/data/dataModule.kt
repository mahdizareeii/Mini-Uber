package com.shared.miniuber.data

import com.shared.miniuber.data.repository.FakeDriverRepositoryImpl
import com.shared.miniuber.domain.repository.DriverRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    factory {
        FakeDriverRepositoryImpl()
    } bind DriverRepository::class
}