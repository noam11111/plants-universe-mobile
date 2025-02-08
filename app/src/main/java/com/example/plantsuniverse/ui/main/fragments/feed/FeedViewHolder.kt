package com.example.plantsuniverse.ui.main.fragments.feed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsuniverse.databinding.PostBinding
import com.example.plantsuniverse.data.posts.Post
import com.example.plantsuniverse.data.posts.PostWithOwner

class FeedViewHolder(
    private val binding: PostBinding,
    private val isEditable: Boolean,
    listener: AdapterView.OnItemClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    private var post: PostWithOwner? = null

    init {
        //TODO: add click listener
    }

    fun bind(post: PostWithOwner, position: Int) {
        this.post = post

        if (post.post.photo != "") {
            val decodedString: ByteArray = Base64.decode(post.post.photo, Base64.DEFAULT)
            val bitmap: Bitmap =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.postImage.setImageBitmap(bitmap)
        }

        binding.postTitle.text = post.post.text
        binding.postOwner.text = post.owner.username

        if (!isEditable) {
            binding.btnEdit.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
        }
    }
}