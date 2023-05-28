package com.example.eventsapp.fragments

import android.annotation.SuppressLint
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
import android.net.Uri
import android.util.Log
import com.example.eventsapp.UploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class NewEventFragment : Fragment() {
    private lateinit var binding: FragmentNewEventBinding
    private lateinit var imageUrl: String
    private lateinit var ticketUrl: String
    private var userId: Int = 0
    private lateinit var username: String
    private var localization: String? = null

    private val chosenImagesList: ArrayList<Uri> = ArrayList()

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

        userId = arguments?.getInt("id")!!
        username = arguments?.getString("username")!!
        localization = arguments?.getString("localization")

        binding.backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_newEventFragment_to_userEventsFragment, bundle)
        }

        binding.pickImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        binding.addFile.setOnClickListener {
            pickImageFromGallery()
        }

        binding.localizationButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            if (localization != null) {
                bundle.putString("localization", localization)
            }
            findNavController().navigate(R.id.action_newEventFragment_to_newEventMapFragment, bundle)
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
                        imageUrl = uploadedFileResponse!!.imageUrl

                        val chosenFile = binding.chosenTicket
                        val fileBitmap = (chosenFile.drawable as BitmapDrawable).bitmap
                        val ticketFile = File(requireContext().cacheDir, "image2.jpg")
                        val fileOutputStream = FileOutputStream(ticketFile)
                        fileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                        fileOutputStream.flush()
                        fileOutputStream.close()
                        val requestTicketFile = ticketFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageTicketPart = MultipartBody.Part.createFormData("file", ticketFile.name, requestTicketFile)

                        val ticketFileCall = apiService.uploadImage(imageTicketPart)

                        ticketFileCall.enqueue(object: Callback<UploadResponse> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<UploadResponse>,
                                response: Response<UploadResponse>
                            ) {
                                if (response.isSuccessful){
                                    val uploadedTicketFileResponse: UploadResponse? = response.body()
                                    ticketUrl = uploadedTicketFileResponse!!.imageUrl

                                    val event = NewEvent(
                                        title = binding.newEventTitle.text.toString(),
                                        description = binding.newEventDescription.text.toString(),
                                        imageUrl = imageUrl,
                                        fileUrl = ticketUrl,
                                        localization = localization!!,
                                        eventLink = binding.newEventLink.text.toString(),
                                        date = binding.newEventDate.text.toString(),
                                        userId = userId
                                    )

                                    val newEventCall = apiService.createUserEvent(userId, event)


                                    newEventCall.enqueue(object : Callback<Event> {
                                        override fun onResponse(call: Call<Event>, response: Response<Event>) {
                                            if (response.isSuccessful){
                                                val bundle = Bundle()
                                                bundle.putInt("id", userId)
                                                bundle.putString("username", username)
                                                findNavController().navigate(R.id.action_newEventFragment_to_userEventsFragment, bundle)
                                            }
                                        }

                                        override fun onFailure(call: Call<Event>, t: Throwable) {
                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }
                            }

                            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                t.printStackTrace()
                            }

                        })
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            })


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
            val imageUri: Uri? = data?.data
            imageUri?.let {
                chosenImagesList.add(it)
            }
            if (chosenImagesList.size <= 1){
                binding.chosenImage.setImageURI(imageUri)
            } else {
                binding.chosenTicket.setImageURI(imageUri)
            }
        }
    }

}