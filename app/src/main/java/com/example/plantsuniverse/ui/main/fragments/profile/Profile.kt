package com.example.plantsuniverse.ui.main.fragments.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantsuniverse.R
import com.example.plantsuniverse.data.users.User
import com.example.plantsuniverse.databinding.FragmentProfileBinding
import com.example.plantsuniverse.ui.auth.AuthActivity
import com.example.plantsuniverse.ui.main.PostsViewModel
import com.example.plantsuniverse.ui.main.Utils
import com.example.plantsuniverse.ui.main.fragments.feed.FeedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.UUID

class Profile : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private val viewModel: PostsViewModel by viewModels()
    private var galleryLauncher: ActivityResultLauncher<Intent>? = null
    private var isEditing = false
    private val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
    private var user: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding?.root

        if (binding != null) {
            galleryLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        data?.data?.let { uri ->
                            val inputStream = requireContext().contentResolver.openInputStream(uri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding!!.profilePicture.setImageBitmap(bitmap)

                            val base64String = Utils.convertImageToBase64(bitmap)
                            val newUser =
                                User(user!!.id, user!!.username, base64String, user!!.email)
                            user = newUser
                            viewModel.updateUser(
                                newUser
                            )
                        }
                    }
                }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.let { profileView ->
            super.onViewCreated(view, savedInstanceState)
            profileView.profilePostsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())

            viewModel.getAllPostsByUserId(userId).observe(viewLifecycleOwner, {
                profileView.profilePostsRecyclerView.adapter =
                    it?.let { it1 ->
                        FeedAdapter(it1, true,
                            onDeletePost = { post ->
                                viewModel.deletePostById(post.id)
                            },
                            onEditPost = { post ->
                                val action = ProfileDirections.actionProfileToCreatePost(post)
                                Navigation.findNavController(view).navigate(action)
                            })
                    }
            })

            viewModel.getUserById(userId).observe(viewLifecycleOwner, {
                if (it != null) {
                    profileView.usernameText.text = it.username
                    profileView.usernameEditText.setText(it.username)
                    if (it.photo != "") {
                        val decodedString: ByteArray =
                            Base64.decode(it.photo, Base64.DEFAULT)
                        val bitmap: Bitmap =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        profileView.profilePicture.setImageBitmap(bitmap)
                    }
                    user = it
                }
            })

            profileView.profilePicture.setOnClickListener {
                if (isEditing) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    galleryLauncher?.launch(intent)
                }
            }


            profileView.profilePageLogoutButton.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            profileView.profilePageEditButton.setOnClickListener {

                if (isEditing) {
                    val newUsername = profileView.usernameEditText.text.toString()
                    profileView.usernameText.text = newUsername

                    // Change button to "Edit"
                    profileView.profilePageEditButton.icon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)

                    // Hide the EditText, show the TextView
                    profileView.usernameText.visibility = View.VISIBLE
                    profileView.usernameEditText.visibility = View.GONE

                    if (user != null) {
                        viewModel.updateUser(
                            User(
                                user!!.id,
                                newUsername,
                                user!!.photo,
                                user!!.email
                            )
                        )
                    }

                } else {
                    // Switch to edit mode
                    profileView.usernameText.visibility = View.GONE
                    profileView.usernameEditText.visibility = View.VISIBLE

                    profileView.profilePageEditButton.icon =
                        ContextCompat.getDrawable(requireContext(), R.drawable.ic_save)

                }

                // Toggle the editing state
                isEditing = !isEditing

            }
        }
    }
}