package com.rpm.rpmmovil.Routes.RecyclerPlaces

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class PlacesAdapter(private var places: List<String>) : RecyclerView.Adapter<PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_places, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        // Llama al método bind con el nombre del lugar en la posición actual
        holder.bind(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun updatePlacesList(newPlaces: List<String>) {
        places = newPlaces

        notifyDataSetChanged()
    }
}




