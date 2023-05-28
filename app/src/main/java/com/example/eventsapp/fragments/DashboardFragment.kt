package com.example.eventsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentDashboardBinding
import com.example.eventsapp.databinding.FragmentLoginBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private var userId: Int = 0
    private lateinit var username: String


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
