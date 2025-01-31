package com.example.plantsuniverse.room

import android.content.Context
import androidx.room.Room

object DatabaseHolder {
    private var appDatabase: Database? = null

    fun initDatabase(context: Context) {
        appDatabase = Room.databaseBuilder(context, Database::class.java, "plants-universe-db").fallbackToDestructiveMigration()
            .build()
    }

    fun getDatabase(): Database {
        return this.appDatabase!!
    }
}