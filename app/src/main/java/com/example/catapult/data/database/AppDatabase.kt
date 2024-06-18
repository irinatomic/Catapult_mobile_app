package com.example.catapult.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catapult.data.database.dao.*
import com.example.catapult.data.database.entities.*

@Database(
    entities = [
        BreedDbModel :: class,
        ImageDbModel :: class,
        ResultDbModel :: class,
        LBItemDbModel :: class
    ],
    version = 5,
    exportSchema = true,
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun breedDao(): BreedDao

    abstract fun imageDao(): ImageDao

    abstract fun resultDao(): ResultDao

    abstract fun leaderboardDao(): LBItemDao
}