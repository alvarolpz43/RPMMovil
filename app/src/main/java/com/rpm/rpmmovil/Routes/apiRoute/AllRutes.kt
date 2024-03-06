package com.rpm.rpmmovil.Routes.apiRoute

import com.google.gson.annotations.SerializedName

data class AllRutes( @SerializedName("rutas") val ruta:List<Route>)

data class Route(
    @SerializedName("NombreRuta") val NombreRuta: String,
    @SerializedName("PuntoInicioRuta") val PuntoInicioRuta: String,
    @SerializedName("PuntoFinalRuta") val PuntoFinalRuta: String,
    @SerializedName("KmTotalesRuta") val KmTotalesRuta: String,
    @SerializedName("PresupuestoGas") val PresupuestoGas: Number,
    @SerializedName("FotoRuta") val FotoRuta: String,
    @SerializedName("DescripcionRuta") val DescripcionRuta: String,
    @SerializedName("CalificacionRuta") val CalificacionRuta: Number,
    @SerializedName("motoviajero") val motoviajero: String,




    )
