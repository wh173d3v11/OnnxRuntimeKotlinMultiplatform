package com.fierydinesh.kmp.sharedlayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform