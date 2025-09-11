package com.android.miniuber

import com.android.miniuber.core.coreModule
import com.android.miniuber.data.dataModule
import com.android.miniuber.domain.domainModule
import com.android.miniuber.feature.featureModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

expect fun getPlatform(): Platform

interface Platform {
    val name: String
    fun generateUuid(): String
}

fun initKoin(koinContext: (KoinApplication) -> Unit) {
    startKoin {
        koinContext.invoke(this)
        modules(getKoinModules())
    }
}

fun getKoinModules() = listOf(
    featureModule,
    domainModule,
    dataModule,
    coreModule
)