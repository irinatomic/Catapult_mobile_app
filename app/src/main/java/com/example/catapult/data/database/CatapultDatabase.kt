package com.example.catapult.data.database

import android.content.Context
import androidx.room.Room

object CatapultDatabase {

    lateinit var database: AppDatabase

    fun initDatabase(context: Context) {
        database = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "rma.db"
        ).fallbackToDestructiveMigration()
         .build()
    }
}