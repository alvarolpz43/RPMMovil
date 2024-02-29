package com.rpm.rpmmovil.ExplorarRutas.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class rutaAdapter(private val images:List<String>):RecyclerView.Adapter<rutaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rutaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return rutaViewHolder(layoutInflater.inflate(R.layout.item_rutas,parent,false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: rutaViewHolder, position: Int) {
        val item=images[position]
        holder.bind(item)
    }
}