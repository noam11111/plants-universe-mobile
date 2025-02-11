package com.example.plantsuniverse.data.posts

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): LiveData<List<PostWithOwner>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun findById(id: String): LiveData<PostWithOwner?>

    @Query("SELECT * FROM posts WHERE owner_id =:ownerId")
    fun getPostsByOwnerId(ownerId: String): LiveData<List<PostWithOwner>?>

    @Delete
    fun delete(post: Post)

    @Query("DELETE FROM posts WHERE id = :id")
    fun deleteById(id: String)

    @Update
    fun updatePost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(post: Post)

    @Upsert
    fun upsertPost(post: Post)

    @Upsert
    fun upsertAll(vararg post: Post)
}