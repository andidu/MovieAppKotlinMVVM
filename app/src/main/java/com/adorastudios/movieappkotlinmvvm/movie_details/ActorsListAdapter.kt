package com.adorastudios.movieappkotlinmvvm.movie_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.data.Actor

class ActorsListAdapter :
    ListAdapter<Actor, ActorsListAdapter.ActorViewHolder>(
        DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_actor, parent, false))
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val actorName: TextView = itemView.findViewById(R.id.textViewActor)
        private val actorImage: ImageView = itemView.findViewById(R.id.imageViewActor)

        fun onBind(actor: Actor) {
            actorName.text = actor.name
            actorImage.load(actor.imageUrl)
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem == newItem
        }

    }
}