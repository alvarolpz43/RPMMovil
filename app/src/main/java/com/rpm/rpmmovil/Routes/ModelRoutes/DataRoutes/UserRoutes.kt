package com.rpm.rpmmovil.Routes.apiRoute

import com.google.gson.annotations.SerializedName

data class UserRoutes(
    @SerializedName("rutas") val rutas: List<Routes>
)


data class Routes(
    @SerializedName("_id") val rutaid: String,
    @SerializedName("NombreRuta") val NombreRuta: String,
    @SerializedName("PuntoInicioRuta") val PuntoInicioRuta: String,
    @SerializedName("PuntoFinalRuta") val PuntoFinalRuta: String,
    @SerializedName("KmTotalesRuta") val KmTotalesRuta: Number,
    @SerializedName("PresupuestoGas") val PresupuestoGas: Number,
    @SerializedName("FotoRuta") val FotoRuta: String,
    @SerializedName("DescripcionRuta") val DescripcionRuta: String,
    @SerializedName("CalificacionRuta") val CalificacionRuta: Float,
    @SerializedName("motoviajero") val motoviajero: String

) {

}