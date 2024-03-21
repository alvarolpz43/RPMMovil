package com.rpm.rpmmovil.Routes.RecyclerView


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

import com.rpm.rpmmovil.Routes.apiRoute.Routes

class RoutesAdapter(
    var userRoutes: List<Routes> = emptyList(),
    private val onItemSelected: (String) -> Unit

) : RecyclerView.Adapter<RoutesView>() {

    fun updatelist(list: List<Routes>) {
        userRoutes = list ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesView {
        return RoutesView(
            LayoutInflater.from(parent.context).inflate(R.layout.item_routes, parent, false)
        )
    }

    override fun getItemCount() = userRoutes.size


    override fun onBindViewHolder(holder: RoutesView, position: Int) {
        holder.bind(userRoutes[position],onItemSelected)
    }


}