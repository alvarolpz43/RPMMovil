package com.rpm.rpmmovil.Routes.RecyclerPlaces

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val placeNameTextView: TextView = itemView.findViewById(R.id.placeNameTextView)
    private var selectedPlace: String? = null

    fun bind(placeName: String) {
        placeNameTextView.text = placeName





        // Agregar OnClickListener al itemView
        itemView.setOnClickListener {
            // Mostrar un Toast cuando se haga clic en el elemento
            Toast.makeText(itemView.context, "Lugar seleccionado: $placeName", Toast.LENGTH_SHORT).show()

            val pInicioEditText = (itemView.context as Activity).findViewById<EditText>(R.id.pInicio)


            // Establecer el texto del EditText con el nombre del lugar seleccionado
            pInicioEditText.setText(placeName)

        }





    }






}



