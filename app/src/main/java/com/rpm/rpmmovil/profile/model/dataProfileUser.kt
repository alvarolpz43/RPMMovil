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





//"_id": "65e621f7ad61e875824069a0",
//"Nombres_Mv": "luis",
//"Email_Mv": "luis@gmail.com",
//"NumeroIdent_Mv": 100,
//"FechaNac_Mv": "15-10-2002",
//"Contraseña_Mv": "$2a$10$lc7AFjfrEobIzaYI1Km55e5DReIqPAaVwnX5FDz5Wbc9AfJpTwfxy",
//"NumeroTel_Mv": 300,
//"createdAt": "2024-03-04T19:33:11.452Z",
//"updatedAt": "2024-03-04T19:33:11.452Z",
//"__v": 0


