package com.example.recipesapp.fragments.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.eventsapp.Event
import com.example.eventsapp.databinding.RecyclerViewEventsBinding
import com.squareup.picasso.Picasso

class UserEventsRecyclerAdapter(private val userEvents: ArrayList<Event>) : RecyclerView.Adapter<UserEventsRecyclerAdapter.MyViewHolder>() {

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: onItemClickListener? = null

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    inner class MyViewHolder(binding: RecyclerViewEventsBinding, listener: onItemClickListener) : ViewHolder(binding.root) {
        val eventTitle = binding.eventTitle
        val eventDate = binding.eventDate
        val eventImage = binding.eventImage

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
        holder.eventTitle.text = userEvents[position].title
        holder.eventDate.text = userEvents[position].date
        Picasso.get().load(userEvents[position].imageUrl).into(holder.eventImage)
    }

    override fun getItemCount(): Int {
        return userEvents.size
    }
}