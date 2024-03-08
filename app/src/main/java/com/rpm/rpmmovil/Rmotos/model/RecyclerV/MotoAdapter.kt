package com.rpm.rpmmovil.Rmotos.model.RecyclerV

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rpm.rpmmovil.R
import com.rpm.rpmmovil.Rmotos.model.Data.DataItemMotos

class MotoAdapter(var motolist: List<DataItemMotos> = emptyList(),
    private  val onItemSelected: (String) -> Unit
    ) :
        RecyclerView.Adapter<motoViewHolder>(){
            fun updatelist(List: List<DataItemMotos>){
                motolist = List ?: emptyList()
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): motoViewHolder {
        return  motoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_motos,parent,false)
        )
    }

    override fun onBindViewHolder(holder: motoViewHolder, position: Int) {
        holder.bind(motolist[position])
    }

    override fun getItemCount() = motolist.size


        }