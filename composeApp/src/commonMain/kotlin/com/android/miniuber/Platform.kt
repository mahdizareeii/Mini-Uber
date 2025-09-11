package com.android.miniuber

interface Platform {
    val name: String
    fun generateUuid(): String
}

expect fun getPlatform(): Platform