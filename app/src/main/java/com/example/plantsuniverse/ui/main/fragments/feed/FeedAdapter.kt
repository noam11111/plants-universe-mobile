package com.example.plantsuniverse.ui.main.fragments.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsuniverse.databinding.PostBinding
import com.example.plantsuniverse.data.posts.Post
import com.example.plantsuniverse.data.posts.PostWithOwner

class FeedAdapter(
    private val posts: List<PostWithOwner>,
    private val isEditable: Boolean = false,
    private val onDeletePost: (Post) -> Unit = {},
    private val onEditPost: (Post) -> Unit = {}
) : RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return FeedViewHolder(binding, isEditable, onDeletePost, onEditPost)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size
}
