package com.android.miniuber

import platform.Foundation.NSUUID
import platform.UIKit.UIDevice

actual fun getPlatform(): Platform = IOSPlatform()

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun generateUuid(): String = NSUUID().UUIDString()
}