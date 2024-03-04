package com.rpm.rpmmovil.Rmotos.model

data class DataRMotos(val MotoNombre:String, val  ModeloMoto:String, val MarcaMoto:String, val  VersionMoto:Int, val ConsumoMotoLx100km:Int, val  CilindrajeMoto:String, val FotoMoto:String)

// MotoNombre: body.MotoNombre,
//        ModeloMoto: body.ModeloMoto,
//        MarcaMoto: body.MarcaMoto,
//        VersionMoto: body.VersionMoto,
//        ConsumoMotoLx100km: parseInt(body.ConsumoMotoLx100km),
//        CilindrajeMoto: body.CilindrajeMoto,
//        FotoMoto: downloadURL,