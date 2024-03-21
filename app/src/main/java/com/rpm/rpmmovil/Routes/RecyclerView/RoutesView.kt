
package com.rpm.rpmmovil.Routes.RecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.rpm.rpmmovil.Routes.apiRoute.Routes
import com.rpm.rpmmovil.databinding.ItemRoutesBinding
import com.squareup.picasso.Picasso


class RoutesView (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemRoutesBinding.bind(view)


    fun bind(userRoutes: Routes,onItemSelected:(String)->Unit){

        Picasso.get().load(userRoutes.FotoRuta).fit()
            .into(binding.ivRutas)

        binding.nomruta.text = userRoutes.NombreRuta
        binding.kmtotalesruta.text = "${userRoutes.KmTotalesRuta?.toString()} Km" ?: "N/A"
        binding.presupuestogas.text = "Presupuesto combustible: $ ${userRoutes.PresupuestoGas?.toString()}" ?: "N/A"
        binding.calificacionruta.rating = userRoutes.CalificacionRuta ?: 0.0f
        binding.verRuta.setOnClickListener { onItemSelected(userRoutes.rutaid) }

        //datos que no uso por ahora
//        binding.puntoinicioruta.text = userRoutes.PuntoInicioRuta
//        binding.puntofinalruta.text = userRoutes.PuntoFinalRuta
//        binding.descripruta.text = userRoutes.DescripcionRuta




    }

}