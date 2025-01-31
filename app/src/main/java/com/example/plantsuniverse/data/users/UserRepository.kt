package com.example.plantsuniverse.data.users

import com.example.plantsuniverse.room.DatabaseHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository {

    private val usersDao = DatabaseHolder.getDatabase().usersDao()
    private val firestoreHandle = Firebase.firestore.collection("users")

    suspend fun upsertUser(user: User) = withContext(Dispatchers.IO) {
        firestoreHandle.document(user.id).set(user).await()
        usersDao.upsertUser(user)
    }
}