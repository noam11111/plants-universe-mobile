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
import com.example.plantsuniverse.retrofit.PlantsRepository
import com.example.plantsuniverse.ui.main.PostsViewModel
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream
import android.media.ExifInterface

class createPost : Fragment() {
    private val plantsRepository = PlantsRepository()
    private val viewModel: PostsViewModel by activityViewModels()
    private var userId: String = FirebaseAuth.getInstance().currentUser!!.uid

    private lateinit var imageView: ImageView
    private lateinit var base64Image: String

    private var post: Post? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        post = arguments?.getParcelable("post")

        post?.let {
            Log.d("noam", "Received Post: $it")
        } ?: Log.d("noam", "No post received")

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
//            viewModel.addPost(newPost)

            viewModel.savePost(newPost)
            val action = createPostDirections.actionCreatePostFragmentToFeed()
            Navigation.findNavController(it).navigate(action)
        }

    }

    val tempPlantsSpecies = arrayOf("Cactus", "Fern", "Succulent", "Rose", "Tulip")

    private fun populateSpinner(spinner: Spinner) {
        plantsRepository.getPlants { result ->

            if (result.isSuccess) {
                val plants = result.getOrNull()

                val plantTypes: Array<String> = plants?.map { it.common_name }?.toTypedArray() ?: tempPlantsSpecies

                updatePlantsSpecies(plantTypes, spinner)


            } else {
                updatePlantsSpecies(tempPlantsSpecies, spinner)
            }
        }
    }

    private fun updatePlantsSpecies(plantTypes: Array<String>, spinner: Spinner) {
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

        val rotatedBitmap = fixImageRotation(uri, originalBitmap)

        val scaledBitmap = Bitmap.createScaledBitmap(rotatedBitmap, 480, 480, true) // Adjust width/height

        val compressedStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, compressedStream) // Lower quality to 70%
        val compressedByteArray = compressedStream.toByteArray()

        return Base64.encodeToString(compressedByteArray, Base64.DEFAULT)
    }

    private fun fixImageRotation(uri: Uri, bitmap: Bitmap): Bitmap {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val exif = ExifInterface(inputStream!!)

        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        val matrix = android.graphics.Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}