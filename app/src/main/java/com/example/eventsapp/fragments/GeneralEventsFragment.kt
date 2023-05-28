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
import com.example.eventsapp.GeneralEvent
import com.example.eventsapp.R
import com.example.eventsapp.adapters.GeneralEventsRecyclerAdapter
import com.example.eventsapp.databinding.FragmentGeneralEventsBinding
import com.example.eventsapp.databinding.FragmentUserEventsBinding
import com.example.recipesapp.fragments.Adapters.UserEventsRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GeneralEventsFragment : Fragment() {
    private lateinit var binding: FragmentGeneralEventsBinding
    private lateinit var generalEvents: ArrayList<GeneralEvent>
    private lateinit var mRecyclerViewAdapter: GeneralEventsRecyclerAdapter

    private var userId: Int = 0
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralEventsBinding.inflate(inflater, container, false)

        userId = arguments?.getInt("id")!!
        username = arguments?.getString("username")!!

        generalEvents = ArrayList()

        mRecyclerViewAdapter = GeneralEventsRecyclerAdapter(generalEvents)
        binding.generalEventsRecyclerView.layoutManager = LinearLayoutManager(context)

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api: ApiService = retrofit.create(ApiService::class.java)

        val generalEventsCall: Call<ArrayList<GeneralEvent>> = api.getGeneralEvents()

        generalEventsCall.enqueue(object: Callback<ArrayList<GeneralEvent>?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ArrayList<GeneralEvent>?>,
                response: Response<ArrayList<GeneralEvent>?>
            ) {
                if (response.isSuccessful){
                    generalEvents.clear()
                    for (event in response.body()!!){
                        generalEvents.add(event)
                    }
                    binding.generalEventsRecyclerView.adapter?.notifyDataSetChanged()

                    binding.generalEventsRecyclerView.adapter = mRecyclerViewAdapter

                }
            }

            override fun onFailure(call: Call<ArrayList<GeneralEvent>?>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })

        binding.backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_generalEventsFragment_to_dashboardFragment, bundle)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerViewAdapter.setOnItemClickListener(object : GeneralEventsRecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
