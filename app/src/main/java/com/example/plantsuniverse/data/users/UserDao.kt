package com.example.plantsuniverse.data.users

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface UserDao {

    @Query("SELECT * FROM Users")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id =:id")
    fun getUserById(id: String): User

    @Delete
    fun delete(student: User)

    @Update
    fun updateUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Upsert
    fun upsertUser(user: User)
}