package com.example.eventsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eventsapp.Event
import com.example.eventsapp.GeneralEvent
import com.example.eventsapp.databinding.RecyclerViewEventsBinding
import com.example.eventsapp.databinding.RecyclerViewGeneralEventsBinding
import com.example.recipesapp.fragments.Adapters.UserEventsRecyclerAdapter
import com.squareup.picasso.Picasso

class GeneralEventsRecyclerAdapter(private val generalEvents: ArrayList<GeneralEvent>) : RecyclerView.Adapter<GeneralEventsRecyclerAdapter.MyViewHolder>(){

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: onItemClickListener? = null

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    inner class MyViewHolder(binding: RecyclerViewGeneralEventsBinding, listener: onItemClickListener) : ViewHolder(binding.root) {
        val eventTitle = binding.eventTitle
        val eventDate = binding.eventDate
        val eventImage = binding.eventImage

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralEventsRecyclerAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val recyclerViewGeneralEventBinding = RecyclerViewGeneralEventsBinding.inflate(inflater, parent, false)
        return MyViewHolder(recyclerViewGeneralEventBinding, mListener!!)
    }

    override fun onBindViewHolder(holder: GeneralEventsRecyclerAdapter.MyViewHolder, position: Int) {
        holder.eventTitle.text = generalEvents[position].title
        holder.eventDate.text = generalEvents[position].date
        Picasso.get().load(generalEvents[position].imageUrl).into(holder.eventImage)
    }

    override fun getItemCount(): Int {
        return generalEvents.size
    }
}