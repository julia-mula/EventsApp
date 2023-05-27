package com.example.eventsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentAllEventsMapBinding
import com.example.eventsapp.databinding.FragmentDashboardBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class AllEventsMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentAllEventsMapBinding
    private var userId: Int = 0
    private lateinit var username: String

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllEventsMapBinding.inflate(inflater, container, false)

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

        // Add markers to the map
        val point1 = LatLng(37.7749, -122.4194)
        val point2 = LatLng(37.7858, -122.4064)

        googleMap.addMarker(
            MarkerOptions()
                .position(point1)
                .title("Point 1")
                .snippet("This is the description for Point 1")
        )
        googleMap.addMarker(
            MarkerOptions()
                .position(point2)
                .title("Point 2")
                .snippet("This is the description for Point 2")
        )

        // Move the camera to the first point
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point1, 12f))
    }
}