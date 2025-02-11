package com.example.plantsuniverse.ui.main.fragments.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsuniverse.databinding.FragmentFeedBinding
import com.example.plantsuniverse.ui.main.PostsViewModel

class Feed : Fragment() {
    private var binding: FragmentFeedBinding? = null
    private val viewModel: PostsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.feedRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getAllPosts().observe(viewLifecycleOwner, {
            if (it.isEmpty()) viewModel.loadPosts()
            binding?.feedRecyclerView?.adapter = FeedAdapter(it)
        })
    }
}