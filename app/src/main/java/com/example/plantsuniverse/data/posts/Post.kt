package com.example.plantsuniverse.data.posts

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.plantsuniverse.data.users.User
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "posts",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["owner_id"]
    )])

@Parcelize
data class Post(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "owner_id") val ownerId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "photo")val photo: String?
) : Parcelable {
    companion object {

        private const val ID_KEY = "id"
        private const val OWNER_KEY = "ownerId"
        private const val TEXT_KEY = "text"
        private const val PHOTO_KEY = "photo"

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY] as? String ?: ""
            val ownerId = json[OWNER_KEY] as? String ?: ""
            val text = json[TEXT_KEY] as? String ?: ""
            val photo = json[PHOTO_KEY] as? String ?: null

            return Post(
                id = id,
                ownerId = ownerId,
                text = text,
                photo = photo
            )
        }
    }

    val json: HashMap<String, Any?>
        get() {
            return hashMapOf(
                ID_KEY to id,
                OWNER_KEY to ownerId,
                TEXT_KEY to text,
                PHOTO_KEY to photo
            )
        }

}