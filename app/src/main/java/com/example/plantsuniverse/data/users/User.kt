package com.example.plantsuniverse.data.users


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val photo: String,
    val email: String,
) {

    companion object {

        private const val ID_KEY = "id"
        private const val USER_NAME_KEY = "username"
        private const val PHOTO_KEY = "photo"
        private const val EMAIL_KEY = "email"

        fun fromJSON(json: Map<String, Any>): User {
            val id = json[ID_KEY] as? String ?: ""
            val username = json[USER_NAME_KEY] as? String ?: ""
            val photo = json[PHOTO_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""

            return User(
                id = id,
                username = username,
                photo = photo,
                email = email
            )
        }

        fun fromFirebaseAuth(): User {
            val user = FirebaseAuth.getInstance().currentUser

            return User(
                id = user?.uid!!,
                email = user.email!!,
                username = user.displayName!!,
                photo = ""
            )
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID_KEY to id,
                USER_NAME_KEY to username,
                PHOTO_KEY to photo,
                EMAIL_KEY to email)
        }
}
