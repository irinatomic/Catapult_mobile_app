package com.example.catapult

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CatapultApp: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}