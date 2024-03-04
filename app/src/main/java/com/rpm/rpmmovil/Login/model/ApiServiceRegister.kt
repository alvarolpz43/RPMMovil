package com.rpm.rpmmovil.Login.model


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceRegister {
    @POST("register")
    fun getRegister(@Body register:DtaRegister):Call<DtaRegister>
}