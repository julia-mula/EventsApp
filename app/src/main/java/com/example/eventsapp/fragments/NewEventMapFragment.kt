package com.example.eventsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eventsapp.R
import com.example.eventsapp.databinding.FragmentAllEventsMapBinding
import com.example.eventsapp.databinding.FragmentNewEventMapBinding


class NewEventMapFragment : Fragment() {
    private lateinit var binding: FragmentNewEventMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewEventMapBinding.inflate(inflater, container, false)



        return binding.root
    }
}