package com.example.recipesapp.fragments.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eventsapp.Event
import com.example.eventsapp.databinding.RecyclerViewEventsBinding

class UserEventsRecyclerAdapter(private val userEvents: ArrayList<Event>) : RecyclerView.Adapter<UserEventsRecyclerAdapter.MyViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: onItemClickListener? = null

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    inner class MyViewHolder(binding: RecyclerViewEventsBinding, listener: onItemClickListener) : ViewHolder(binding.root) {
            val eventText = binding.eventText

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val recyclerViewRecipeBinding = RecyclerViewEventsBinding.inflate(inflater, parent, false)
        return MyViewHolder(recyclerViewRecipeBinding, mListener!!)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.eventText.text = userEvents[position].title
    }

    override fun getItemCount(): Int {
        return userEvents.size
    }
}