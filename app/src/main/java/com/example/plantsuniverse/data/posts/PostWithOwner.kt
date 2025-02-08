package com.example.plantsuniverse.data.posts

import androidx.room.Embedded
import androidx.room.Relation
import com.example.plantsuniverse.data.users.User

data class PostWithOwner(
    @Embedded val post: Post,
    @Relation(
        entity = User::class,
        parentColumn = "owner_id",
        entityColumn = "id"
    ) val owner: User
) {
}