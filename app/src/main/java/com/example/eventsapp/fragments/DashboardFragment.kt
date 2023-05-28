package com.example.eventsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.ApiService
import com.example.eventsapp.Event
import com.example.eventsapp.GeneralEvent
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentDashboardBinding
import com.example.eventsapp.databinding.FragmentLoginBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private var userId: Int = 0
    private lateinit var username: String
    private lateinit var randomEvent: GeneralEvent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        userId = arguments?.getInt("id")!!
        username = arguments?.getString("username")!!

        binding.dashboardUsername.text = username

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api: ApiService = retrofit.create(ApiService::class.java)

        val randomEventCall: Call<GeneralEvent> = api.getRandomEvent()

        randomEventCall.enqueue(object: Callback<GeneralEvent> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<GeneralEvent>,
                response: Response<GeneralEvent>
            ) {
                if (response.isSuccessful){
                    randomEvent = response.body()!!
                    Picasso.get().load(randomEvent.imageUrl).into(binding.eventImage)
                    binding.eventDate.text = randomEvent.date
                    binding.eventTitle.text = randomEvent.title
                }
            }

            override fun onFailure(call: Call<GeneralEvent>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })

        binding.userEventsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_dashboardFragment_to_userEventsFragment, bundle)
        }

        binding.allEventsMapButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_dashboardFragment_to_allEventsMapFragment, bundle)
        }

        binding.generalEventsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_dashboardFragment_to_generalEventsFragment, bundle)
        }

        return binding.root
    }
}
