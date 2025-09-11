package com.android.miniuber.util

interface Platform {
    val name: String
    fun generateUuid(): String
}

expect fun getPlatform(): Platform