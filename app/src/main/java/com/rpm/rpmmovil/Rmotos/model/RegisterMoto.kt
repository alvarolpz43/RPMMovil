package com.rpm.rpmmovil.Rmotos.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterMoto {
    @POST("motos")
    fun PostRegisterMoto(@Body register:DataRMotos): Call<DataRMotos>
}