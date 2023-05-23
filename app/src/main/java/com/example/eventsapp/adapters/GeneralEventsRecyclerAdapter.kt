package com.example.eventsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eventsapp.Event
import com.example.eventsapp.databinding.RecyclerViewEventsBinding
import com.example.recipesapp.fragments.Adapters.UserEventsRecyclerAdapter

class GeneralEventsRecyclerAdapter(private val generalEvents: ArrayList<Event>) : RecyclerView.Adapter<GeneralEventsRecyclerAdapter.MyViewHolder>(){

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: GeneralEventsRecyclerAdapter.onItemClickListener? = null

    fun setOnItemClickListener(listener: GeneralEventsRecyclerAdapter.onItemClickListener){
        mListener = listener
    }

    inner class MyViewHolder(binding: RecyclerViewEventsBinding, listener: GeneralEventsRecyclerAdapter.onItemClickListener) : ViewHolder(binding.root) {
        val eventText = binding.eventText

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralEventsRecyclerAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val recyclerViewEventBinding = RecyclerViewEventsBinding.inflate(inflater, parent, false)
        return MyViewHolder(recyclerViewEventBinding, mListener!!)
    }

    override fun onBindViewHolder(holder: GeneralEventsRecyclerAdapter.MyViewHolder, position: Int) {
        holder.eventText.text = generalEvents[position].title
    }

    override fun getItemCount(): Int {
        return generalEvents.size
    }
}