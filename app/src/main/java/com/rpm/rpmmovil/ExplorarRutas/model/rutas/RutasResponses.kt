package com.rpm.rpmmovil.ExplorarRutas.model.rutas

import com.google.gson.annotations.SerializedName

data class RutasResponses(
    var ruta: RutasData
)

data class RutasData(
    @SerializedName("_id") val rutaid: String,
    @SerializedName("NombreRuta") val nomruta: String,
    @SerializedName("PuntoInicioRuta") val puntoiniruta: String,
    @SerializedName("PuntoFinalRuta") val puntofinalruta: String,
    @SerializedName("KmTotalesRuta") val kmstotruta: Number,
    @SerializedName("PresupuestoGas") val pptogas: Number,
    @SerializedName("FotoRuta") val images: String,
    @SerializedName("DescripcionRuta") val descripruta: String,
    @SerializedName("CalificacionRuta") val califruta: Number
)
