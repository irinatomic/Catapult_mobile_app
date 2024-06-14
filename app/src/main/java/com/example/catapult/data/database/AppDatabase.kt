package com.example.catapult.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catapult.data.database.dao.*
import com.example.catapult.data.database.entities.*

@Database(
    entities = [
        UserDbModel :: class,
        BreedDbModel :: class,
        ImageDbModel :: class,
        ResultDbModel :: class,
        LBItemDbModel :: class
    ],
    version = 4,
    exportSchema = true,
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun breedDao(): BreedDao

    abstract fun imageDao(): ImageDao

    abstract fun resultDao(): ResultDao

    abstract fun leaderboardDao(): LBItemDao
}