package com.rpm.rpmmovil.Routes.RecyclerPlaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class PlacesAdapter2(private var places: List<String>) : RecyclerView.Adapter<PlaceViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder2 {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_places, parent, false)
        return PlaceViewHolder2(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder2, position: Int) {
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
