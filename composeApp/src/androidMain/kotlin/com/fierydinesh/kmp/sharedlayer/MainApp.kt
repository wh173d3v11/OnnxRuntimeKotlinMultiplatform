package com.fierydinesh.kmp.sharedlayer

import android.app.Application

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        lateinit var appInstance: MainApp
    }
}