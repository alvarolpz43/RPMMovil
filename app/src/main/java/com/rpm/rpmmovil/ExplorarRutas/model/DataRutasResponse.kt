
package com.rpm.rpmmovil.ExplorarRutas.model

import com.google.gson.annotations.SerializedName

data class DataRutasRespose(
    @SerializedName("ruta") val ruta: List<DataRutasItemRespose>
)

data class DataRutasItemRespose(
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

//data class RutaImageResponse(@SerializedName("url") val url: String)



