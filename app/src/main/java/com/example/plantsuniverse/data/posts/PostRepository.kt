package com.example.plantsuniverse.data.posts

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.plantsuniverse.data.users.UserRepository
import com.example.plantsuniverse.room.DatabaseHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostRepository() {
    private val postsDao = DatabaseHolder.getDatabase().postsDao()
    private val userRepository = UserRepository()
    private val firestoreHandle = Firebase.firestore.collection("posts")

    suspend fun addPost(vararg post: Post) = withContext(Dispatchers.IO) {
        val batchHandle = Firebase.firestore.batch()
        post.forEach {
            batchHandle.set(firestoreHandle.document(it.id), it)
            postsDao.upsertPost(it)
        }

        batchHandle.commit().await()
    }

    suspend fun editPost(post: Post) = withContext(Dispatchers.IO) {
        firestoreHandle.document(post.id).set(post).await()
        postsDao.insertPosts(post)
    }

    suspend fun deletePostById(id: String) = withContext(Dispatchers.IO) {
        firestoreHandle.document(id).delete().await()
        postsDao.deleteById(id)
    }

    fun getPostById(id: String): LiveData<PostWithOwner?> {
        return postsDao.findById(id)
    }

    fun getPostsByOwnerId(id: String): LiveData<List<PostWithOwner>?> {
        return postsDao.getPostsByOwnerId(id)
    }

    fun getPosts(
    ): LiveData<List<PostWithOwner>> {
        return postsDao.getAllPosts()
    }

    suspend fun loadPostsFromRemoteSource() =
        withContext(Dispatchers.IO) {
            postsDao.deleteAll()

            val posts = firestoreHandle.get().await().toObjects(PostDTO::class.java)
                .map { it.toPostModel() }

            if (posts.isNotEmpty()) {
                userRepository.cacheUsersIfNotExisting(posts.map { it.ownerId })
                postsDao.upsertAll(*posts.toTypedArray())
            }
        }
}