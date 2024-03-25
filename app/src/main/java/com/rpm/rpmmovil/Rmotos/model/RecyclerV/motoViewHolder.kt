package com.rpm.rpmmovil.Rmotos.model.RecyclerV

import DataItemMotos
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.Rmotos.ShowGarageActivity
import com.rpm.rpmmovil.Rmotos.UpdatesMotos.ViewsUpdateMotos
import com.rpm.rpmmovil.databinding.ItemMotosBinding
import com.squareup.picasso.Picasso


class motoViewHolder(
    view: View,
    private val adapter: MotoAdapter  // Agrega una instancia de MotoAdapter como parámetro
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMotosBinding.bind(view)


    fun bind(datamotos: DataItemMotos) {
        binding.motonom.text = datamotos.motonom
        binding.motomodelo.text = datamotos.motomodel
        binding.marcamoto.text = datamotos.motomarca
        binding.versionmoto.text = datamotos.motovers.toString() ?: "N/A"
        binding.consumokmxg.text = datamotos.consumokmxg.toString() ?: "N/A"
        binding.cilimoto.text = datamotos.cilimoto
        Picasso.get().load(datamotos.imagemoto).into(binding.ivMotos)


        binding.btnEditar.setOnClickListener {
            val idMoto = datamotos._id
           Toast.makeText(itemView.context, "Editando a $idMoto", Toast.LENGTH_SHORT).show()

            val intent = Intent(itemView.context, ViewsUpdateMotos::class.java)
            intent.putExtra("idMoto", datamotos._id)
            intent.putExtra("motonom", datamotos.motonom)
            intent.putExtra("motomodel", datamotos.motomodel)
            intent.putExtra("motomarca", datamotos.motomarca)
            intent.putExtra("motovers", datamotos.motovers.toString())
            intent.putExtra("consumokmxg", datamotos.consumokmxg.toString())
            intent.putExtra("cilimoto", datamotos.cilimoto)
            itemView.context.startActivity(intent)
        }





        binding.btnEliminar.setOnClickListener {

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val dataItemMotos = adapter.motolist[position]
                val idMoto = dataItemMotos._id
                val context = itemView.context
                if (context is ShowGarageActivity) {
                    context.deleteMoto(idMoto)
                }
            }
        }
    }


}