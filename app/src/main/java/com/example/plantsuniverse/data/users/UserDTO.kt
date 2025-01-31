package com.example.plantsuniverse.data.users

data class UserDTO(

    val email: String = "",
    val username: String = "",
    val photo : String? = "",
    val id: String? = null
) {
    fun toUserModel(): User {
        return User(
            id=id ?: "",
            email = email,
            username = username,
            photo = photo ?: ""
        )
    }
}