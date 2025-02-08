package com.example.plantsuniverse.data.posts

import android.graphics.Bitmap

data class PostDTO(
    val id: String? = null,
    val ownerId: String = "",
    val text: String,
    val photo: String? = null
) {
    constructor() : this("", "", "", "")

    fun toPostModel(): Post {
        return Post(
            id = id ?: "",
            ownerId = ownerId,
            text = text,
            photo = photo
        )
    }
}