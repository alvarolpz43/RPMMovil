package com.rpm.rpmmovil.ExplorarRutas.model

data class DataRutas(
    val NombreRuta: String,
    val PuntoInicioRuta: String,
    val PuntoFinalRuta: String,
    val KmTotalesRuta: Number,
    val PresupuestoGas: Number,
    val FotoRuta: String,
    val DescripcionRuta: String,
    val CalificacionRuta: Number
)
