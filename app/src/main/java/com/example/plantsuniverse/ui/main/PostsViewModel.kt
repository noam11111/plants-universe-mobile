package com.example.plantsuniverse.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantsuniverse.data.posts.Post
import com.example.plantsuniverse.data.posts.PostRepository
import com.example.plantsuniverse.data.posts.PostWithOwner
import com.example.plantsuniverse.data.users.User
import com.example.plantsuniverse.data.users.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PostsViewState(val postId: String = "")

class PostsViewModel : ViewModel() {
    private val repository = PostRepository()
    private val usersRepository = UserRepository()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadPostsFromRemoteSource()
        }
    }

    fun getAllPosts(): LiveData<List<PostWithOwner>> {
        return this.repository.getPosts()
    }

    fun getAllPostsByUserId(id: String): LiveData<List<PostWithOwner>?> {
        return this.repository.getPostsByOwnerId(id)
    }

    fun loadPosts() {
        viewModelScope.launch {
            repository.loadPostsFromRemoteSource()
        }
    }

    fun getUserById(id: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val user = withContext(Dispatchers.IO) {
                usersRepository.getUserByid(id)
            }
            userLiveData.postValue(user)
        }
        return userLiveData
    }

    fun updateUser(
        user: User,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            usersRepository.upsertUser(user)
        }
    }

    fun addPost(
        post: Post,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.addPost(post)
        }
    }

    fun savePost(
        post: Post,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.editPost(post)
        }
    }

    fun deletePostById(
        postId: String,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deletePostById(postId)
        }
    }


}