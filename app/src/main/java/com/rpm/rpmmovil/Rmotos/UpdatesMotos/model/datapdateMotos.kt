package com.rpm.rpmmovil.Rmotos.UpdatesMotos.model

import com.google.gson.annotations.SerializedName
import com.rpm.rpmmovil.profile.model.dataUser

data class dataUpdatemoto(
    @SerializedName("motos") val motoFound: DataMoto
)
data class DataMoto(
    @SerializedName("_id") val _id: String,
    @SerializedName("MotoNombre") val motonombre: String,
    @SerializedName("MarcaMoto") val motomarca: String,
    @SerializedName("ModeloMoto") val motomodel: String,
    @SerializedName("VersionMoto") val motoversion: Number,
    @SerializedName("ConsumoMotoLx100km") val consumokmxg: Number,
    @SerializedName("CilindrajeMoto") val cilimoto: String,
    @SerializedName("FotoMoto") val imagemoto: String
)

//{
//    "_id": "65f1da7676d7c10141e0932a",
//    "MotoNombre": "latal",
//    "MarcaMoto": "yamaha ",
//    "ModeloMoto": "fz",
//    "VersionMoto": 2020,
//    "ConsumoMotoLx100km": 2,
//    "CilindrajeMoto": "156",
//    "FotoMoto": "https://firebasestorage.googleapis.com/v0/b/rpm-img.appspot.com/o/imagenes%2Fimagen_1710348894758.jpg?alt=media&token=053afbb9-2b55-445d-b40f-c01e4795a43b",
//    "motoviajero": "65f1d8ab76d7c10141e09324",
//    "createdAt": "2024-03-13T16:55:18.849Z",
//    "updatedAt": "2024-03-13T16:55:18.849Z",
//    "__v": 0
//},