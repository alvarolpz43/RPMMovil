package com.rpm.rpmmovil.Rmotos.UpdatesMotos.model

import com.google.gson.annotations.SerializedName
import com.rpm.rpmmovil.profile.model.dataUser

data class dataUpdatemoto(
    @SerializedName("foundMoto") val motoFound: DataItemUpdateMotos
)
data class DataItemUpdateMotos(
    @SerializedName("_id") val _id: String,
    @SerializedName("MotoNombre") val motonombre: String,
    @SerializedName("MarcaMoto") val motomarca: String,
    @SerializedName("ModeloMoto") val motomodel: String,
    @SerializedName("VersionMoto") val motoversion: Number,
    @SerializedName("ConsumoMotoLx100km") val consumokmxg: Number,
    @SerializedName("CilindrajeMoto") val cilimoto: String,
    @SerializedName("FotoMoto") val imagemoto: String
)