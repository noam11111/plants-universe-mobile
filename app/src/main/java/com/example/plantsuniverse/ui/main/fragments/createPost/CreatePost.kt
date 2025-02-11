package com.example.plantsuniverse.ui.main.fragments.createPost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.plantsuniverse.R
import com.example.plantsuniverse.data.posts.Post
import com.example.plantsuniverse.ui.main.PostsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream

class createPost : Fragment() {

    private val viewModel: PostsViewModel by activityViewModels()
    private var userId: String = FirebaseAuth.getInstance().currentUser!!.uid

    private lateinit var imageView: ImageView
    private lateinit var base64Image: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Noam - createPost", "noammmm")
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val content = view.findViewById<TextView>(R.id.content_edit_text)
        val plantTypeSpinner = view.findViewById<Spinner>(R.id.plant_type_spinner)
        imageView = view.findViewById<ImageView>(R.id.postImage)

        populateSpinner(plantTypeSpinner)

        imageView.setOnClickListener {
            pickImageFromGallery()
        }

        val addPostButton = view.findViewById<Button>(R.id.create_reviewer_button)

        addPostButton.setOnClickListener {
            val selectedPlantType = plantTypeSpinner.selectedItem.toString()
            val newPost = Post(
                ownerId = userId,
                text = "${content.text}\n#${selectedPlantType}",
                photo = base64Image
            )
            viewModel.addPost(newPost)
            val action = createPostDirections.actionCreatePostFragmentToFeed()
            Navigation.findNavController(it).navigate(action)
        }

    }

    private fun populateSpinner(spinner: Spinner) {
        // Plant types list to populate the spinner
        val plantTypes = arrayOf("Cactus", "Fern", "Succulent", "Rose", "Tulip")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            plantTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            uri?.let {
                // Display the image
                imageView.setImageURI(uri)

                // Convert image to Base64
                base64Image = convertImageToBase64(uri)
            }
        }
    }

    private fun convertImageToBase64(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        // Reduce the dimensions of the image
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 480, 480, true) // Adjust width/height

        // Compress the Bitmap
        val compressedStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, compressedStream) // Lower quality to 30%
        val compressedByteArray = compressedStream.toByteArray()

        // Convert to Base64
        return Base64.encodeToString(compressedByteArray, Base64.DEFAULT)
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}