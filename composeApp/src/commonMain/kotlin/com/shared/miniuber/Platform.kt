package com.shared.miniuber

import com.shared.miniuber.core.coreModule
import com.shared.miniuber.data.dataModule
import com.shared.miniuber.domain.domainModule
import com.shared.miniuber.feature.featureModule
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