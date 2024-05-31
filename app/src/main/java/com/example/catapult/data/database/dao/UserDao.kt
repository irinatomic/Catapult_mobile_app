package com.example.catapult.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catapult.data.database.entities.UserDbModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userDbModel: UserDbModel)

    @Query("SELECT * FROM UserDbModel")
    suspend fun getAll(): List<UserDbModel>

    @Query("SELECT * FROM UserDbModel WHERE id = :id")
    suspend fun getUserById(id: Int): UserDbModel

    @Query("SELECT * FROM UserDbModel WHERE nickname = :nickname")
    suspend fun getUserByNickname(nickname: String): UserDbModel
}