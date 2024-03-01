package com.rpm.rpmmovil.ExplorarRutas.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.databinding.ItemRutasBinding
import com.squareup.picasso.Picasso

class rutaViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val binding = ItemRutasBinding.bind(view)
    fun bind(image:String){
        Picasso.get().load(image).into(binding.ivRutas)
    }
}