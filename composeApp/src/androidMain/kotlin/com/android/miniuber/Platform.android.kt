package com.android.miniuber

import android.os.Build
import java.util.UUID

actual fun getPlatform(): Platform = AndroidPlatform()

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override fun generateUuid(): String = UUID.randomUUID().toString()
}