package com.example.plantsuniverse.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.plantsuniverse.data.posts.Post
import com.example.plantsuniverse.data.posts.PostDao
import com.example.plantsuniverse.data.users.User
import com.example.plantsuniverse.data.users.UserDao

@Database(
    entities = [User::class, Post::class], version = 3, exportSchema = true

)
abstract class Database : RoomDatabase() {
    abstract fun usersDao(): UserDao
    abstract fun postsDao(): PostDao
}