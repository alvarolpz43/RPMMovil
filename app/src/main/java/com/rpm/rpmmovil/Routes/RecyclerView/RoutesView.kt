package com.rpm.rpmmovil.Routes.RecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos
import com.rpm.rpmmovil.Routes.apiRoute.Route
import com.rpm.rpmmovil.databinding.ItemMotosBinding
import com.rpm.rpmmovil.databinding.ItemRoutesBinding
import com.squareup.picasso.Picasso

class RoutesView (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRoutesBinding.bind(view)

    fun bind(userRoutes: Route){
        binding.nombreRuta.text= userRoutes.NombreRuta
        binding.pInicio.text= userRoutes.PuntoInicioRuta
        binding.pFinal.text= userRoutes.PuntoFinalRuta
        binding.km.text= userRoutes.KmTotalesRuta.toString() ?: "N/A"

    }

}