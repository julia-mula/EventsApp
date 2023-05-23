package com.example.eventsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentNewEventBinding
import com.example.eventsapp.databinding.FragmentUserEventsBinding

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

        return binding.root
    }

}