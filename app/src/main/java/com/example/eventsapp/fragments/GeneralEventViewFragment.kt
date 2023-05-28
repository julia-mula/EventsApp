package com.example.eventsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentGeneralEventViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso


class GeneralEventViewFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentGeneralEventViewBinding
    private var userId: Int = 0
    private lateinit var username: String
    private lateinit var localization: String

    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralEventViewBinding.inflate(inflater, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        userId = arguments?.getInt("id")!!
        username = arguments?.getString("username")!!

        val title = arguments?.getString("title")!!
        val description = arguments?.getString("description")!!
        val imageUrl = arguments?.getString("imageUrl")!!
        localization = arguments?.getString("localization")!!
        val eventLink = arguments?.getString("eventLink")!!
        val date = arguments?.getString("date")!!
        val eventId = arguments?.getInt("eventId")!!

        binding.descriptionText.text = description
        binding.eventLinkText.text = eventLink
        binding.dateText.text = date
        binding.titleText.text = title
        Picasso.get().load(imageUrl).into(binding.eventImage)

        binding.backButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", userId)
            bundle.putString("username", username)
            findNavController().navigate(R.id.action_generalEventViewFragment_to_generalEventsFragment, bundle)
        }

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val parsedLocalization = parseLocation(localization)

        googleMap.addMarker(
            MarkerOptions()
                .position(parsedLocalization)
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parsedLocalization, 12f))
    }


    private fun parseLocation(location: String): LatLng {
        val latLngValues = location.split(", ")
        val latitude = latLngValues[0].toDouble()
        val longitude = latLngValues[1].toDouble()

        return LatLng(latitude, longitude)
    }
}