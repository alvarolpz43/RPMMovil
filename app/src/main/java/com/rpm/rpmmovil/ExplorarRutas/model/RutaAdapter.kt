package com.rpm.rpmmovil.ExplorarRutas.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class RutaAdapter(
    var rutaslist: List<DataRutasItemRespose> = emptyList(),
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<rutaViewHolder>() {
        fun updateList(list: List<DataRutasItemRespose>) {
            rutaslist = list ?: emptyList()
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rutaViewHolder {
        return rutaViewHolder(
       LayoutInflater.from(parent.context).inflate(R.layout.item_rutas,parent,false)
        )
    }

//    override fun onBindViewHolder(holder: rutaViewHolder, position: Int) {
//        holder.bind(rutaslist[position])
//    }
            override fun onBindViewHolder(holder: rutaViewHolder, position: Int) {
                holder.bind(rutaslist[position],onItemSelected)
            }

            override fun getItemCount() = rutaslist.size


}

