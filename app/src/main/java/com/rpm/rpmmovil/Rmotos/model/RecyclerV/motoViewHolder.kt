package com.rpm.rpmmovil.Rmotos.model.RecyclerV

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import com.rpm.rpmmovil.databinding.ItemMotosBinding
import com.squareup.picasso.Picasso

class motoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMotosBinding.bind(view)

    fun bind(datamotos: DataItemMotos){
        binding.motonom.text = datamotos.motonom
        binding.motomodelo.text = datamotos.motomodel
        binding.marcamoto.text = datamotos.motomarca
        binding.versionmoto.text = datamotos.motovers.toString() ?: "N/A"
        binding.consumokmxg.text = datamotos.consumokmxg.toString() ?: "N/A"
        binding.cilimoto.text = datamotos.cilimoto
        Picasso.get().load(datamotos.imagemoto).into(binding.ivMotos)
    }
}