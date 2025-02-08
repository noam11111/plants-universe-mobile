package com.example.plantsuniverse

import android.app.Application
import com.example.plantsuniverse.room.DatabaseHolder

class ApplicationStarter: Application() {
    override fun onCreate() {
        DatabaseHolder.initDatabase(this)
        super.onCreate()
    }
}