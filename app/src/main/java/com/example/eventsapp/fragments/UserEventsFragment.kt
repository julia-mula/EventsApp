package com.example.eventsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventsapp.ApiService
import com.example.eventsapp.Event
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentUserEventsBinding
import com.example.recipesapp.fragments.Adapters.UserEventsRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserEventsFragment : Fragment() {
    private lateinit var binding: FragmentUserEventsBinding
    private var userId: Int = 0
    private lateinit var userEvents: ArrayList<Event>
    private lateinit var mRecyclerViewAdapter: UserEventsRecyclerAdapter

    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserEventsBinding.inflate(inflater, container, false)

        userId = arguments?.getInt("id")!!
        username = arguments?.getString("username")!!

        userEvents = ArrayList()

        mRecyclerViewAdapter = UserEventsRecyclerAdapter(userEvents)
        binding.userEventsRecyclerView.layoutManager = LinearLayoutManager(context)

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api: ApiService = retrofit.create(ApiService::class.java)

        val recipesCall: Call<ArrayList<Event>> = api.getUserEvents(userId!!)

        recipesCall.enqueue(object: Callback<ArrayList<Event>?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<Event>?>,
                response: Response<ArrayList<Event>?>
            ) {
                if (response.isSuccessful){
                    userEvents.clear()
                    for (event in response.body()!!){
                        userEvents.add(event)
                    }
                    binding.userEventsRecyclerView.adapter?.notifyDataSetChanged()

                    binding.userEventsRecyclerView.adapter = mRecyclerViewAdapter

                }
            }

            override fun onFailure(call: Call<ArrayList<Event>?>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })

        binding.backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_userEventsFragment_to_dashboardFragment, bundle)
        }

        binding.addEventButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_userEventsFragment_to_newEventFragment, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerViewAdapter.setOnItemClickListener(object : UserEventsRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val bundle = Bundle()
                val currentEvent: Event = userEvents.get(position)

                bundle.putInt("id", userId)
                bundle.putString("username", username)

                bundle.putString("title", currentEvent.title)
                bundle.putString("description", currentEvent.description)
                bundle.putString("imageUrl", currentEvent.imageUrl)
                bundle.putString("fileUrl", currentEvent.fileUrl)
                bundle.putString("localization", currentEvent.localization)
                bundle.putString("eventLink", currentEvent.eventLink)
                bundle.putString("date", currentEvent.date)
                bundle.putInt("eventId", currentEvent.id)

                findNavController().navigate(R.id.action_userEventsFragment_to_eventViewFragment, bundle)
            }
        })
    }
}
