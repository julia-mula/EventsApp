package com.example.eventsapp.fragments

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
import com.example.eventsapp.databinding.FragmentUserEventsBinding
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class NewEventFragment : Fragment() {
    private lateinit var binding: FragmentNewEventBinding

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

        binding.confirmEventButton.setOnClickListener {

            val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val apiService: ApiService = retrofit.create(ApiService::class.java)

            val event = NewEvent(
                title = "Event 3",
                description = "Description of Event 3",
                imageUrl = "https://example.com/event3.jpg",
                fileUrl = "https://example.com/event3.pdf",
                localization = "Location 3",
                eventLink = "https://example.com/event3",
                date = "2023-05-20 19:00",
                userId = 1
            )

            val call = apiService.createUserEvent(1, event)


            call.enqueue(object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful){
                        findNavController().navigate(R.id.action_newEventFragment_to_userEventsFragment)
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding.root
    }

}