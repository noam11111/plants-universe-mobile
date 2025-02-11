package com.example.plantsuniverse.ui.main.fragments.feed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsuniverse.databinding.PostBinding
import com.example.plantsuniverse.data.posts.PostWithOwner
import com.example.plantsuniverse.data.posts.Post

class FeedViewHolder(
    private val binding: PostBinding,
    private val isEditable: Boolean,
    private val onDeletePost: (Post) -> Unit = {},
    private val onEditPost: (Post) -> Unit = {}
) : RecyclerView.ViewHolder(binding.root) {

    private var post: PostWithOwner? = null

    fun bind(post: PostWithOwner) {
        this.post = post

        if (post.post.photo?.isNotEmpty() == true) {
            val decodedString: ByteArray = Base64.decode(post.post.photo, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.postImage.setImageBitmap(bitmap)
        }

        binding.postTitle.text = post.post.text
        binding.postOwner.text = post.owner.username

        if (!isEditable) {
            binding.btnEdit.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE

        } else {
            binding.btnDelete.setOnClickListener {

                post.let { onDeletePost(it.post) }
            }

            binding.btnEdit.setOnClickListener {
                post.let { onEditPost(it.post) }
            }
        }
    }
}
