package com.rpm.rpmmovil.Usermotos.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiServiceMotouser {
    @GET("usermotos")
    fun getUserMotos(@Header("Authorization") token: String): Call<List<Usermoto>>
}
