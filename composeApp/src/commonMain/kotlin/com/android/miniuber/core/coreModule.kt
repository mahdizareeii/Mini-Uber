package com.android.miniuber.core

import com.android.miniuber.core.base.BaseContract
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    single { ComposeRouter() } bind BaseContract.Router::class
}