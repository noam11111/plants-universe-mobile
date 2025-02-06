package com.example.plantsuniverse.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO: map to entity class
@Entity
data class Post(
    @PrimaryKey val id: String,
    val owner: String, //TODO: change to a user object
    val text: String,
    val photo: Bitmap?
) {
    companion object {

        private const val ID_KEY = "id"
        private const val OWNER_KEY = "owner"
        private const val TEXT_KEY = "text"
        private const val PHOTO_KEY = "photo"

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY] as? String ?: ""
            val owner = json[OWNER_KEY] as? String ?: ""
            val text = json[TEXT_KEY] as? String ?: ""
            val photoBase64 = json[PHOTO_KEY] as? String ?: null
            var photo: Bitmap? = null

            if (photoBase64 != "") {
                val decodedString: ByteArray = Base64.decode(photoBase64, Base64.DEFAULT)
                val bitmap: Bitmap =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                photo = bitmap
            }

            return Post(
                id = id,
                owner = owner,
                text = text,
                photo = photo
            )
        }
    }

    val json: HashMap<String, Any?>
        get() {
            return hashMapOf(
                ID_KEY to id,
                OWNER_KEY to owner,
                TEXT_KEY to text,
                PHOTO_KEY to photo
            )
        }

}