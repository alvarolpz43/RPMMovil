package com.rpm.rpmmovil.SelectMoto.model

import com.google.gson.annotations.SerializedName

data class DtaSelectMoto(
    @SerializedName("_id") val id: String,
    @SerializedName("ModeloMoto") val modelo: String,
    @SerializedName("ConsumoMotoLx100km") val consumo: Int,
    @SerializedName("MotoNombre") val nombre: String
)
