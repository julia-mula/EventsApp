package com.example.eventsapp.fragments

import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.ApiService
import com.example.eventsapp.Event
import com.example.eventsapp.NewEvent
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentNewEventBinding
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.example.eventsapp.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class NewEventFragment : Fragment() {
    private lateinit var binding: FragmentNewEventBinding

    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewEventBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_newEventFragment_to_userEventsFragment)
        }

        binding.pickImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        binding.confirmEventButton.setOnClickListener {

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val apiService: ApiService = retrofit.create(ApiService::class.java)

            val chosenImage = binding.chosenImage
            val bitmap = (chosenImage.drawable as BitmapDrawable).bitmap

            val file = File(requireContext().cacheDir, "image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val uploadImageCall = apiService.uploadImage(imagePart)

            uploadImageCall.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful){
                        val uploadedFileResponse: UploadResponse? = response.body()
                        if (uploadedFileResponse != null){
                            val imageUrl = uploadedFileResponse.imageUrl
                        }
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            })

//            val event = NewEvent(
//                title = "Event 3",
//                description = "Description of Event 3",
//                imageUrl = "https://example.com/event3.jpg",
//                fileUrl = "https://example.com/event3.pdf",
//                localization = "Location 3",
//                eventLink = "https://example.com/event3",
//                date = "2023-05-20 19:00",
//                userId = 1
//            )
//
//            val call = apiService.createUserEvent(1, event)
//
//
//            call.enqueue(object : Callback<Event> {
//                override fun onResponse(call: Call<Event>, response: Response<Event>) {
//                    if (response.isSuccessful){
//                        findNavController().navigate(R.id.action_newEventFragment_to_userEventsFragment)
//                    }
//                }
//
//                override fun onFailure(call: Call<Event>, t: Throwable) {
//                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                }
//            })
        }

        return binding.root
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            binding.chosenImage.setImageURI(data?.data)
        }
    }

}