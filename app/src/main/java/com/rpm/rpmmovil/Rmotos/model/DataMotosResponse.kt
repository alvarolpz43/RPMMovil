package com.rpm.rpmmovil.Rmotos.model

import com.google.gson.annotations.SerializedName

data class DataMotosResponse( @SerializedName("moto") val moto:List<DataItemMotos>)

data class DataItemMotos(
   @SerializedName("MotoNombre") val motonom: String,
   @SerializedName("ModeloMoto") val motomodel: String,
   @SerializedName("MarcaMoto") val motomarca: String,
   @SerializedName("VersionMoto") val motovers: Number,
   @SerializedName("ConsumoMotoLx100km") val consumokmxg: Number,
   @SerializedName("CilindrajeMoto") val cilimoto: String,
   @SerializedName("FotoMoto") val imagemoto:String,
)

