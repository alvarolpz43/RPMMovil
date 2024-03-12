package com.rpm.rpmmovil.Rmotos.model.Data

import com.google.gson.annotations.SerializedName

data class DataMotosResponse( @SerializedName("motos") val moto:List<DataItemMotos>)

data class DataItemMotos(
   @SerializedName("MotoNombre") val motonom: String,
   @SerializedName("MarcaMoto") val motomarca: String,
   @SerializedName("ModeloMoto") val motomodel: String,
   @SerializedName("VersionMoto") val motovers: Number,
   @SerializedName("ConsumoMotoLx100km") val consumokmxg: Number,
   @SerializedName("CilindrajeMoto") val cilimoto: String,
   @SerializedName("FotoMoto") val imagemoto: String,
)
