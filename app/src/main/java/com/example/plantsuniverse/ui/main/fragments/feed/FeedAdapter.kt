package com.example.plantsuniverse.ui.main.fragments.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.plantsuniverse.databinding.PostBinding
import com.example.plantsuniverse.model.Post

class FeedAdapter(
    private val posts: List<Post>, private val isEditable: Boolean = false,
) : RecyclerView.Adapter<FeedViewHolder>() {

    var listener: AdapterView.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflator, parent, false)
        return FeedViewHolder(binding, isEditable, listener)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(posts[position], position)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}