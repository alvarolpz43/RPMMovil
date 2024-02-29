package com.rpm.rpmmovil.ExplorarRutas.model

import com.google.gson.annotations.SerializedName

data class DataRutas(
    val NombreRuta: String,
    val PuntoInicioRuta: String,
    val PuntoFinalRuta: String,
    val KmTotalesRuta: Number,
    val PresupuestoGas: Number,
    @SerializedName("FotoRuta") val images:List<String>,
    val DescripcionRuta: String,
    val CalificacionRuta: Number
)
