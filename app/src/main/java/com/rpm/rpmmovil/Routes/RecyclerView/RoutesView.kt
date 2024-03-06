package com.rpm.rpmmovil.Routes.RecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.Routes.apiRoute.Route
import com.rpm.rpmmovil.databinding.ItemRoutesBinding
import com.squareup.picasso.Picasso


class RoutesView (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRoutesBinding.bind(view)


    fun bind(userRoutes: Route){
        binding.nomruta.text = userRoutes.nomruta
        binding.puntoinicioruta.text = userRoutes.puntoiniruta
        binding.puntofinalruta.text = userRoutes.puntofinalruta
        binding.kmtotalesruta.text = userRoutes.kmstotruta?.toString() ?: "N/A"
        binding.presupuestogas.text = userRoutes.pptogas?.toString() ?: "N/A"
        binding.descripruta.text = userRoutes.descripruta
        binding.calificacionruta.text = userRoutes.califruta?.toString() ?: "N/A"
        Picasso.get().load(userRoutes.images).into(binding.ivRutas)

    }

}