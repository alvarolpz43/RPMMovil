package com.rpm.rpmmovil.Usermotos.model

import retrofit2.http.GET
import retrofit2.http.Header

interface ApiServiceMotouser {
    @GET("usermotos")
    suspend fun getMotos(@Header("Authorization") token: String): MotosResponse
}
