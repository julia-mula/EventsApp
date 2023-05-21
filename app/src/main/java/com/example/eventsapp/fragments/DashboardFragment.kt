package com.example.eventsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentDashboardBinding
import com.example.eventsapp.databinding.FragmentLoginBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.userEventsButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_userEventsFragment)
        }

        return binding.root
    }
}
