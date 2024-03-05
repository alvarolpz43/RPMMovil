package com.rpm.rpmmovil.Rmotos.model.Apis

import com.rpm.rpmmovil.Rmotos.model.Data.DataMotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiServiceMotos {
    @GET("motos")
    suspend fun getAllMotos(@Header("Autorization") token:String): Response<DataMotosResponse>
}