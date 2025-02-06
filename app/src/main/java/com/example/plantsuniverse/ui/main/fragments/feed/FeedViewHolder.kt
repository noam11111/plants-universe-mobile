package com.example.plantsuniverse.ui.main.fragments.feed

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsuniverse.R
import com.example.plantsuniverse.databinding.PostBinding
import com.example.plantsuniverse.model.Post
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class FeedViewHolder(
    private val binding: PostBinding,
    private val isEditable: Boolean,
    listener: AdapterView.OnItemClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    private var post: Post? = null

    init {
        //TODO: add click listener
    }

    fun bind(post: Post, position: Int) {
        this.post = post
        if (post.photo != null) {
            binding.postImage.setImageBitmap(post.photo)
        }
        binding.postTitle.text = post.text
        binding.postOwner.text = post.owner

        if (!isEditable) {
            binding.btnEdit.visibility = View.GONE
            binding.btnDelete.visibility = View.GONE
        }
    }
}