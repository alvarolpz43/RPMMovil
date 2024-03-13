package com.rpm.rpmmovil.Rmotos.model.RecyclerV

import DataItemMotos
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.ViewsUpdateMotos

import com.rpm.rpmmovil.databinding.ItemMotosBinding
import com.squareup.picasso.Picasso



class motoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMotosBinding.bind(view)
    private val btnEditar= binding.btnEditar
    private  val btnEliminar= binding.btnEliminar

    fun bind(datamotos: DataItemMotos){
        binding.motonom.text = datamotos.motonom
        binding.motomodelo.text = datamotos.motomodel
        binding.marcamoto.text = datamotos.motomarca
        binding.versionmoto.text = datamotos.motovers.toString() ?: "N/A"
        binding.consumokmxg.text = datamotos.consumokmxg.toString() ?: "N/A"
        binding.cilimoto.text = datamotos.cilimoto
        Picasso.get().load(datamotos.imagemoto).into(binding.ivMotos)


        binding.btnEditar.setOnClickListener {
            val intent = Intent(itemView.context, ViewsUpdateMotos::class.java)
            intent.putExtra("dataItemMotos", datamotos) // Pasar el objeto DataItemMotos
            itemView.context.startActivity(intent)
            Toast.makeText(itemView.context, "Editando", Toast.LENGTH_SHORT).show()
        }




        binding.btnEliminar.setOnClickListener {

            Toast.makeText(itemView.context, "Eliminando", Toast.LENGTH_SHORT).show()
        }
    }


}