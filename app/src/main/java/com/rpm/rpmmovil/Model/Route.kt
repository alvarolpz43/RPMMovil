package com.rpm.rpmmovil.Model

data class Route(
    val _id: String,
    val puntoInicioRuta: String,
    val puntoFinalRuta: String,
    val kmTotalesRuta: Int,
    val presupuestoGas: Int,
    val FotoRuta: String,
    val descripcionRuta: String,
    val calificacion: Int,
    val motoviajero: String
) {

}