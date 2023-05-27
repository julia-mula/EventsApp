package com.example.eventsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.ApiService
import com.example.eventsapp.Event
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentAllEventsMapBinding
import com.example.eventsapp.databinding.FragmentDashboardBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AllEventsMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentAllEventsMapBinding
    private var userId: Int = 0
    private lateinit var username: String

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private lateinit var userEvents: ArrayList<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllEventsMapBinding.inflate(inflater, container, false)

        userEvents = ArrayList()
        userId = arguments?.getInt("id")!!
        username = arguments?.getString("username")!!

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_allEventsMapFragment_to_dashboardFragment, bundle)
        }

        val supportFragmentManager = (activity as FragmentActivity).supportFragmentManager


        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("http://18.185.8.100/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api: ApiService = retrofit.create(ApiService::class.java)

        val getEventsCall = api.getUserEvents(userId)

        getEventsCall.enqueue(object: Callback<ArrayList<Event>?> {
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
                    for (event in userEvents){
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(parseLocation(event.localization))
                                .title(event.title)
                        )
                    }

                    // Move the camera to the first point
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parseLocation(userEvents[0].localization), 12f))
                }
            }
            override fun onFailure(call: Call<ArrayList<Event>?>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }

        })
    }

    private fun parseLocation(location: String): LatLng{
        return LatLng(37.7749, -122.4194)
//        val latLngValues = location.split(", ")
//        val latitude = latLngValues[0].toDouble()
//        val longitude = latLngValues[1].toDouble()
//
//        return LatLng(latitude, longitude)
    }
}