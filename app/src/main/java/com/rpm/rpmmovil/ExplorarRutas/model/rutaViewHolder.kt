package com.rpm.rpmmovil.ExplorarRutas.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.databinding.ItemRutasBinding
import com.squareup.picasso.Picasso

class rutaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRutasBinding.bind(view)
    fun bind(datarutas: DataRutasItemRespose) {
        binding.nomruta.text = datarutas.nomruta
        binding.puntoinicioruta.text = datarutas.puntoiniruta
        binding.puntofinalruta.text = datarutas.puntofinalruta
        binding.kmtotalesruta.text = datarutas.kmstotruta?.toString() ?: "N/A"
        binding.presupuestogas.text = datarutas.pptogas?.toString() ?: "N/A"
        binding.descripruta.text = datarutas.descripruta
        binding.calificacionruta.text = datarutas.califruta?.toString() ?: "N/A"
        Picasso.get().load(datarutas.images).into(binding.ivRutas)
    }


//    fun  bind(datarutas:DataRutasItemRespose,onItemSelected:(String) -> Unit){
//        binding.nomruta.text = datarutas.nomruta
//        binding.puntoinicioruta.text = datarutas.puntoiniruta
//        binding.puntofinalruta.text = datarutas.puntofinalruta
//        binding.kmtotalesruta.text = datarutas.kmstotruta.toString()
//        binding.presupuestogas.text = datarutas.pptogas.toString()
//        binding.descripruta.text = datarutas.descripruta
//        binding.calificacionruta.text = datarutas.califruta.toString()
//        Picasso.get().load(datarutas.images.url).into(binding.ivRutas)
//        binding.root.setOnClickListener { onItemSelected(datarutas.rutaid) }
//
//    }


}
