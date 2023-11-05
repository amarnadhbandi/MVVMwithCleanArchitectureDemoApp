package com.demo.app.shared

import android.app.Application

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(this)
    }
}