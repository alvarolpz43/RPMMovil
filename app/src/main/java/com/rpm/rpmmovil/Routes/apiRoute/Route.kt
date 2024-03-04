package com.rpm.rpmmovil.Model

data class Route(
    val _id: String,
    val NombreRuta: String,
    val PuntoInicioRuta: String,
    val PuntoFinalRuta: String,
    val KmTotalesRuta: Int,
    val PresupuestoGas: Number,
    val FotoRuta: String,
    val DescripcionRuta: String,
    val CalificacionRuta: Number,
    val motoviajero: String,

)
