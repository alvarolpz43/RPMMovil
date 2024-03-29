package com.rpm.rpmmovil.Rmotos.model.RecyclerV

import DataItemMotos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R


class MotoAdapter(
    var motolist: List<DataItemMotos> = emptyList(),
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<motoViewHolder>() {
    fun updatelist(List: List<DataItemMotos>) {
        motolist = List ?: emptyList()
        notifyDataSetChanged()
    }

    fun removeItems(idsToRemove: List<String>) {
        // Filtrar la lista original para obtener solo los elementos que no están en idsToRemove
        motolist = motolist.filter { !idsToRemove.contains(it._id) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): motoViewHolder {
        return motoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_motos, parent, false),
            this // Pasar una instancia de MotoAdapter
        )
    }

    override fun onBindViewHolder(holder: motoViewHolder, position: Int) {
        holder.bind(motolist[position])
    }

    override fun getItemCount() = motolist.size


}