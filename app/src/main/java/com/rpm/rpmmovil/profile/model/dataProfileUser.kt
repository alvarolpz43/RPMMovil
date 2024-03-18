package com.rpm.rpmmovil.profile.model

import com.google.gson.annotations.SerializedName

data class dataProfileUser(
    @SerializedName("UserFound") val userFound: dataUser
)
data class dataUser(
    @SerializedName("_id") val _id: String,
    @SerializedName("Nombres_Mv") val Nombres_Mv: String,
    @SerializedName("Email_Mv") val Email_Mv: String,
    @SerializedName("NumeroIdent_Mv") val NumeroIdent_Mv: Number,
    @SerializedName("FechaNac_Mv") val FechaNac_Mv: String,
    @SerializedName("Contraseña_Mv") val Contraseña_Mv: String,
    @SerializedName("NumeroTel_Mv") val NumeroTel_Mv: Number,
    @SerializedName("ImageUser") val ImageUser: String

)


