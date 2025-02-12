package com.example.plantsuniverse.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

object Utils {
    fun uploadImage(image: Bitmap, name: String, callback: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("image/$name.jpg")

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString())
            }
        }.addOnFailureListener{
            callback("")
        }
    }

    fun convertImageToBase64(bitmap: Bitmap): String {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 480, 480, true) // Adjust width/height
        val compressedStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, compressedStream) // Lower quality to 30%
        val compressedByteArray = compressedStream.toByteArray()
        return Base64.encodeToString(compressedByteArray, Base64.DEFAULT)
    }

}