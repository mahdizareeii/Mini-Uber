package com.android.miniuber

import android.app.Application
import com.shared.miniuber.initKoin
import org.koin.android.ext.koin.androidContext

class MainAndroidApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin { koinContext ->
            koinContext.androidContext(this@MainAndroidApp)
        }
    }
}