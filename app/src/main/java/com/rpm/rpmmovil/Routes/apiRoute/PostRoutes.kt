package com.rpm.rpmmovil.Routes.apiRoute

import com.google.gson.annotations.SerializedName

data class PostRoutes(
    @SerializedName("NombreRuta") val  NombreRuta: String,
    @SerializedName("PuntoInicioRuta")val PuntoInicioRuta: String,
    @SerializedName("PuntoFinalRuta")val PuntoFinalRuta: String,
    @SerializedName("KmTotalesRuta")val KmTotalesRuta: Double,
    @SerializedName("PresupuestoGas")val PresupuestoGas: Int,
    @SerializedName("FotoRuta")val FotoRuta: String,
    @SerializedName("DescripcionRuta")val DescripcionRuta: String,
    @SerializedName("CalificacionRuta")val CalificacionRuta: Int,
    @SerializedName("motoviajero")val motoviajero: String
) {

}