package com.parceros.tijzi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform