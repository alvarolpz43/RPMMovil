package com.rpm.rpmmovil.Routes.RecyclerPlaces

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R

class PlaceViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val placeNameTextView: TextView = itemView.findViewById(R.id.placeNameTextView)


    fun bind(placeName: String) {
        placeNameTextView.text = placeName





        // Agregar OnClickListener al itemView
        itemView.setOnClickListener {
            // Mostrar un Toast cuando se haga clic en el elemento
            Toast.makeText(itemView.context, "Lugar seleccionado: $placeName", Toast.LENGTH_SHORT).show()

            val pFinalEditText = (itemView.context as Activity).findViewById<EditText>(R.id.pFinal)

            // Establecer el texto del EditText con el nombre del lugar seleccionado
            pFinalEditText.setText(placeName)

        }





    }






}