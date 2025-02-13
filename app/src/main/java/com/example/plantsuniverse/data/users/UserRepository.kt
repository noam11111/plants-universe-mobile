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

    suspend fun insertUserIfNotExists(user: User) = withContext(Dispatchers.IO) {
        val documentSnapshot = firestoreHandle.document(user.id).get().await()
        if (!documentSnapshot.exists()) {
            usersDao.upsertUser(user)
        }
    }

    private suspend fun getUserFromRemoteSource(uid: String): User? =
        withContext(Dispatchers.IO) {
            val user =
                firestoreHandle.document(uid).get().await().toObject(UserDTO::class.java)
                    ?.toUserModel()
            if (user != null) {
                usersDao.upsertAll(user)
            }
            return@withContext user
        }

    suspend fun cacheUsersIfNotExisting(ids: List<String>) = withContext(Dispatchers.IO) {
        val existingUserIds = usersDao.getExistingUserIds(ids)
        val nonExistingUserIds = ids.filter { !existingUserIds.contains(it) }

        this@UserRepository.getUsersFromRemoteSource(nonExistingUserIds)
    }

    suspend fun getUserByid(uid: String): User? = withContext(Dispatchers.IO) {
        val cachedResult = usersDao.getUserById(uid)
        if (cachedResult != null) return@withContext cachedResult

        return@withContext this@UserRepository.getUserFromRemoteSource(uid)
    }

    suspend fun getUsersFromRemoteSource(ids: List<String>): List<User> =
        withContext(Dispatchers.IO) {
            val usersQuery =
                if (ids.isNotEmpty()) firestoreHandle.whereIn("id", ids) else firestoreHandle
            val users =
                usersQuery.get().await().toObjects(UserDTO::class.java).map { it.toUserModel() }
            if (users.isNotEmpty()) {
                usersDao.upsertAll(*users.toTypedArray())
            }
            return@withContext users
        }
}